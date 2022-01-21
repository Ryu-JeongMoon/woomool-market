package com.woomoolmarket.security.jwt.factory;

import static com.woomoolmarket.common.constants.CacheConstants.LOGOUT_KEY_PREFIX;
import static com.woomoolmarket.common.constants.TokenConstants.AUTHORITIES_KEY;

import com.amazonaws.util.Base64;
import com.woomoolmarket.cache.CacheService;
import com.woomoolmarket.common.constants.ExceptionConstants;
import com.woomoolmarket.common.util.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Log4j2
@Primary
@Component
@RequiredArgsConstructor
public class RSA512TokenCreator extends TokenCreator {

  private final CacheService cacheService;

  @Value("${jwt.publicKey}")
  private String publicKeyPlainText;
  @Value("${jwt.privateKey}")
  private String privateKeyPlainText;

  private RSAPublicKey publicKey;
  private RSAPrivateKey privateKey;

  @PostConstruct
  public void keySetUp() throws Exception {
    byte[] publicKeyBytes = Base64.decode(publicKeyPlainText);
    byte[] privateKeyBytes = Base64.decode(privateKeyPlainText);

    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
    privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
  }

  @Override
  public String createAccessToken(Authentication authentication, String authorities, Date accessTokenExpireDate) {
    return Jwts.builder()
      .setSubject(authentication.getName())
      .claim(AUTHORITIES_KEY, authorities)
      .setExpiration(accessTokenExpireDate)
      .signWith(privateKey, SignatureAlgorithm.RS512)
      .compact();
  }

  @Override
  protected String createRefreshToken(Date refreshTokenExpireDate) {
    return Jwts.builder()
      .setExpiration(refreshTokenExpireDate)
      .signWith(privateKey, SignatureAlgorithm.RS512)
      .compact();
  }

  @Override
  protected Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder()
        .setSigningKey(publicKey)
        .build()
        .parseClaimsJws(accessToken)
        .getBody();
    } catch (ExpiredJwtException e) {
      log.info("[WOOMOOL-ERROR] :: Expired Token => {} ", e.getMessage());
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
        .setSigningKey(publicKey)
        .build()
        .parseClaimsJws(token);

      return TokenUtils.isNotExpired(claims);
    } catch (Exception e) {
      log.info("[WOOMOOL-ERROR] :: Invalid Token => {} ", e.getMessage());
    }
    return false;
  }
}
