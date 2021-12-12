package com.woomoolmarket.security.jwt;

import com.woomoolmarket.common.util.TokenUtils;
import com.woomoolmarket.security.jwt.factory.TokenFactory;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final TokenFactory tokenFactory;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws ServletException, IOException {
        String accessToken = TokenUtils.resolveAccessTokenFrom((HttpServletRequest) request);
        if (StringUtils.hasText(accessToken) && tokenFactory.validate(accessToken)) {
            Authentication authentication = tokenFactory.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        String refreshToken = TokenUtils.resolveRefreshTokenFrom((HttpServletRequest) request);
        if (StringUtils.hasText(accessToken) && !tokenFactory.validate(accessToken) && tokenFactory.validate(refreshToken)) {
            Authentication authentication = tokenFactory.getAuthentication(accessToken);
            accessToken = tokenFactory.reissueAccessToken(authentication);
            ((HttpServletResponse) response).setHeader(TokenConstants.AUTHORIZATION_HEADER, accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        fc.doFilter(request, response);
    }
}
