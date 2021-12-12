package com.woomoolmarket.security.jwt.factory;

import static com.woomoolmarket.security.jwt.TokenConstants.AUTHORITIES_KEY;
import static com.woomoolmarket.security.jwt.TokenConstants.LOGOUT_KEY_PREFIX;

import com.woomoolmarket.redis.RedisUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
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

    private final RedisUtils redisUtils;

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

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
        }
    }

    @Override
    protected boolean isBlocked(String token) {
        return StringUtils.hasText(token) && StringUtils.hasText(redisUtils.getData(LOGOUT_KEY_PREFIX + token));
    }

    @Override
    protected boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.info("[WOOMOOL-ERROR] :: Invalid Token => {} ", e.getMessage());
        }
        return false;
    }
}
