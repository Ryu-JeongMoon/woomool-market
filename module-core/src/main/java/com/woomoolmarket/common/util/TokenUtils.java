package com.woomoolmarket.common.util;

import com.woomoolmarket.common.constants.TokenConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtils {

    public static String resolveAccessTokenFrom(HttpServletRequest request) {
        String token = request.getHeader(TokenConstants.AUTHORIZATION_HEADER);
        return StringUtils.hasText(token) && token.startsWith(TokenConstants.BEARER_TYPE) ? token.substring(7) : "";
    }

    public static String resolveRefreshTokenFrom(HttpServletRequest request) {
        Cookie refreshCookie = CookieUtils.getCookie(request, TokenConstants.REFRESH_TOKEN)
            .orElseGet(() -> new Cookie("EMPTY", ""));

        String refreshToken = refreshCookie.getValue();
        return StringUtils.hasText(refreshToken) ? refreshToken : "";
    }

    public static boolean isNotExpired(Jws<Claims> claims) {
        return claims.getBody()
            .getExpiration()
            .after(new Date());
    }
}
