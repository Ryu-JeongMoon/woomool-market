package com.woomoolmarket.security.jwt;

import com.woomoolmarket.common.constants.TokenConstants;
import com.woomoolmarket.common.util.TokenUtils;
import com.woomoolmarket.security.jwt.factory.TokenFactory;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final TokenFactory tokenFactory;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

    String accessToken = TokenUtils.resolveAccessTokenFrom(request);
    if (StringUtils.hasText(accessToken) && tokenFactory.validate(accessToken)) {
      Authentication authentication = tokenFactory.getAuthentication(accessToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);
      return;
    }

    String refreshToken = TokenUtils.resolveRefreshTokenFrom(request);
    if (StringUtils.hasText(accessToken) && !tokenFactory.validate(accessToken) && tokenFactory.validate(refreshToken)) {
      Authentication authentication = tokenFactory.getAuthentication(accessToken);
      accessToken = tokenFactory.reissueAccessToken(authentication);
      response.setHeader(TokenConstants.AUTHORIZATION_HEADER, accessToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }
}
