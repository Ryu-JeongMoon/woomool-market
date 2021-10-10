package com.woomoolmarket.security.jwt;

import com.woomoolmarket.redis.RedisUtil;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.security.dto.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Log4j2
@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    private static final String BEARER_TYPE = "Bearer";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final RedisUtil redisUtil;

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰 생성
    public TokenResponse createToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        Date currentDate = new Date();
        long currentTime = currentDate.getTime();
        Date accessTokenExpireDate = new Date(currentTime + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpireDate = new Date(currentTime + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = createAccessToken(authentication, authorities, accessTokenExpireDate);
        String refreshToken = createRefreshToken(refreshTokenExpireDate);

        return TokenResponse.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .accessTokenExpiresIn(ACCESS_TOKEN_EXPIRE_TIME)
            .build();
    }

    private String createAccessToken(Authentication authentication, String authorities, Date accessTokenExpireDate) {
        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .setExpiration(accessTokenExpireDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    private String createRefreshToken(Date refreshTokenExpireDate) {
        return Jwts.builder()
            .setExpiration(refreshTokenExpireDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserPrincipal principal = new UserPrincipal(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validate(String token) {
        return !isBlocked(token) && isValid(token);
    }

    // redis block list 에 해당 토큰 있는지 확인
    private boolean isBlocked(String token) {
        return StringUtils.hasText(token) && StringUtils.hasText(redisUtil.getHashData("logout", token));
    }

    // 토큰의 유효성 + 만료일자 확인
    private boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
        return false;
    }

    public String resolveTokenFrom(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE) ? bearerToken.substring(7) : null;
    }
}
