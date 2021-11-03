package com.woomoolmarket.security.jwt.factory;

import static com.woomoolmarket.security.jwt.TokenConstant.*;

import com.amazonaws.util.Base64;
import com.woomoolmarket.redis.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
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
public class RSA512TokenFactory extends TokenFactory {

    private final RedisUtil redisUtil;

    @Value("${jwt.privateKey}")
    private String privateKeyPlainText;

    @Value("${jwt.publicKey}")
    private String publicKeyPlainText;

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    @PostConstruct
    public void keySetUp() throws Exception {
        byte[] privateKeyBytes = Base64.decode(privateKeyPlainText);
        byte[] publicKeyBytes = Base64.decode(publicKeyPlainText);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
        publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
    }

    @Override
    protected String createAccessToken(Authentication authentication, String authorities, Date accessTokenExpireDate) {
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
            return e.getClaims();
        }
    }

    // redis block list 에 해당 토큰 있는지 확인
    @Override
    protected boolean isBlocked(String token) {
        return StringUtils.hasText(token) && StringUtils.hasText(redisUtil.getData(LOGOUT_KEY_PREFIX + token));
    }

    // 토큰의 유효성 + 만료일자 확인
    @Override
    protected boolean isValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token);

            return claims.getBody()
                .getExpiration()
                .after(new Date());
        } catch (Exception e) {
            log.error(e);
        }
        return false;
    }
}
