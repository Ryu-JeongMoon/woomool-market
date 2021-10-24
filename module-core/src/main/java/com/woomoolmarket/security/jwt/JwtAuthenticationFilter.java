package com.woomoolmarket.security.jwt;

import com.woomoolmarket.security.jwt.factory.TokenFactory;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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
        String token = tokenFactory.resolveTokenFrom((HttpServletRequest) request);
        String requestURI = ((HttpServletRequest) request).getRequestURI();

        if (StringUtils.hasText(token) && tokenFactory.validate(token)) {
            Authentication authentication = tokenFactory.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        fc.doFilter(request, response);
    }
}
