package com.woomoolmarket.security.jwt.factory;

import static com.woomoolmarket.common.constants.CacheConstants.LOGOUT_KEY_PREFIX;
import static com.woomoolmarket.common.constants.TokenConstants.AUTHORITIES_KEY;

import com.woomoolmarket.cache.CacheService;
import com.woomoolmarket.common.constants.ExceptionConstants;
import com.woomoolmarket.common.util.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Log4j2
@Component
@RequiredArgsConstructor
public class HS512TokenFactory extends TokenFactory {

  private final CacheService cacheService;

  private Key key;

  @Value("${jwt.secret}")
  private String secretKey;


  @PostConstruct
  public void keySetUp() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  @Override
  protected String createAccessToken(Authentication authentication, String authorities, Date accessTokenExpireDate) {
    return Jwts.builder()
      .setSubject(authentication.getName())
      .claim(AUTHORITIES_KEY, authorities)
      .setExpiration(accessTokenExpireDate)
      .signWith(key, SignatureAlgorithm.HS512)
      .compact();
  }

  @Override
  protected String createRefreshToken(Date refreshTokenExpireDate) {
    return Jwts.builder()
      .setExpiration(refreshTokenExpireDate)
      .signWith(key, SignatureAlgorithm.HS512)
      .compact();
  }

  @Override
  protected Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(accessToken)
        .getBody();
    } catch (ExpiredJwtException e) {
      log.info("[WOOMOOL-ERROR] :: Invalid Token => {} ", e.getMessage());
      return e.getClaims();
    } catch (MalformedJwtException me) {
      log.info("[WOOMOOL-ERROR] :: Malformed Token => {} ", me.getMessage());
      throw new IllegalArgumentException(ExceptionConstants.TOKEN_NOT_VALID);
    }
  }

  @Override
  protected boolean isBlocked(String token) {
    return StringUtils.hasText(token) && StringUtils.hasText(cacheService.getData(LOGOUT_KEY_PREFIX + token));
  }

  @Override
  protected boolean isValid(String token) {
    try {
      Jws<Claims> claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token);

      return TokenUtils.isNotExpired(claims);
    } catch (Exception e) {
      log.info("[WOOMOOL-ERROR] :: Invalid Token => {} ", e.getMessage());
    }
    return false;
  }
}
