package com.woomoolmarket.security.filter;

import com.woomoolmarket.security.jwt.verifier.ApplicationTokenVerifier;
import com.woomoolmarket.util.TokenFilterHelper;
import com.woomoolmarket.util.TokenUtils;
import com.woomoolmarket.util.constants.UriConstants;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class ApplicationAuthenticationFilter extends OncePerRequestFilter {

  private final TokenFilterHelper tokenFilterHelper;
  private final ApplicationTokenVerifier tokenVerifier;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return PatternMatchUtils.simpleMatch(UriConstants.SHOULD_NOT_FILTER_URL_PATTERNS, request.getRequestURI());
  }

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain chain) throws ServletException, IOException {

    String idToken = TokenUtils.resolveIdToken(request);
    if (!tokenVerifier.isLocalToken(idToken)) {
      chain.doFilter(request, response);
      return;
    }

    if (!tokenFilterHelper.setAuthenticationIfValid(tokenVerifier, idToken))
      forwardToRenew(request, response);

    chain.doFilter(request, response);
  }

  private void forwardToRenew(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String idToken = TokenUtils.resolveIdToken(request);
    String refreshToken = tokenFilterHelper.getRefreshTokenByIdToken(tokenVerifier, idToken);
    String targetUri = String.format(
      "/renewal/redirect?redirect_uri=%s&refresh_token=%s", request.getRequestURI(), refreshToken);
    request.getRequestDispatcher(targetUri).forward(request, response);
  }
}
