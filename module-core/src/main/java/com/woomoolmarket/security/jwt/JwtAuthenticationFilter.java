package com.woomoolmarket.security.jwt;

import com.woomoolmarket.util.constants.TokenConstants;
import com.woomoolmarket.util.TokenUtils;
import com.woomoolmarket.security.jwt.factory.TokenCreator;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final TokenCreator tokenCreator;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

    String accessToken = TokenUtils.resolveAccessTokenFrom(request);
    if (StringUtils.hasText(accessToken) && tokenCreator.validate(accessToken)) {
      Authentication authentication = tokenCreator.getAuthentication(accessToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);
      return;
    }

    String refreshToken = TokenUtils.resolveRefreshTokenFrom(request);
    if (StringUtils.hasText(accessToken) && !tokenCreator.validate(accessToken) && tokenCreator.validate(refreshToken)) {
      Authentication authentication = tokenCreator.getAuthentication(accessToken);
      accessToken = tokenCreator.reissueAccessToken(authentication);
      response.setHeader(TokenConstants.AUTHORIZATION_HEADER, accessToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }
}
