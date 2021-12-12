package com.woomoolmarket.security.jwt.factory;

import static com.woomoolmarket.security.jwt.TokenConstants.ACCESS_TOKEN_EXPIRE_MILLIS;
import static com.woomoolmarket.security.jwt.TokenConstants.AUTHORITIES_KEY;
import static com.woomoolmarket.security.jwt.TokenConstants.BEARER_TYPE;
import static com.woomoolmarket.security.jwt.TokenConstants.REFRESH_TOKEN_EXPIRE_MILLIS;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.security.dto.UserPrincipal;
import io.jsonwebtoken.Claims;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public abstract class TokenFactory {

    protected abstract String createAccessToken(Authentication authentication, String authorities, Date accessTokenExpireDate);

    protected abstract String createRefreshToken(Date refreshTokenExpireDate);

    protected abstract Claims parseClaims(String accessToken);

    protected abstract boolean isValid(String token);

    protected abstract boolean isBlocked(String token);

    public TokenResponse createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        Date currentDate = new Date();
        long currentTime = currentDate.getTime();
        Date accessTokenExpireDate = new Date(currentTime + ACCESS_TOKEN_EXPIRE_MILLIS);
        Date refreshTokenExpireDate = new Date(currentTime + REFRESH_TOKEN_EXPIRE_MILLIS);

        String accessToken = createAccessToken(authentication, authorities, accessTokenExpireDate);
        String refreshToken = createRefreshToken(refreshTokenExpireDate);

        return TokenResponse.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .accessTokenExpiresIn(ACCESS_TOKEN_EXPIRE_MILLIS)
            .build();
    }

    public String reissueAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        Date currentDate = new Date();
        long currentTime = currentDate.getTime();
        Date accessTokenExpireDate = new Date(currentTime + ACCESS_TOKEN_EXPIRE_MILLIS);

        return createAccessToken(authentication, authorities, accessTokenExpireDate);
    }

    public Authentication getAuthentication(String accessToken) {
        Objects.requireNonNull(accessToken);
        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserPrincipal principal = new UserPrincipal(claims.getSubject(), "", Status.ACTIVE, authorities);
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    public boolean validate(String token) {
        return !isBlocked(token) && isValid(token);
    }
}
