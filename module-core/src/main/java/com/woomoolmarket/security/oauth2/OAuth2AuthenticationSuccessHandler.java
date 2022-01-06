package com.woomoolmarket.security.oauth2;

import static com.woomoolmarket.common.constants.CacheConstants.LOGIN_ACCESS_TOKEN_PREFIX;
import static com.woomoolmarket.common.constants.CacheConstants.LOGIN_REFRESH_TOKEN_PREFIX;
import static com.woomoolmarket.common.constants.TokenConstants.ACCESS_TOKEN_EXPIRE_SECONDS;
import static com.woomoolmarket.common.constants.TokenConstants.AUTHORIZATION_HEADER;
import static com.woomoolmarket.common.constants.TokenConstants.REFRESH_TOKEN;
import static com.woomoolmarket.common.constants.TokenConstants.REFRESH_TOKEN_EXPIRE_SECONDS;
import static com.woomoolmarket.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.woomoolmarket.cache.CacheService;
import com.woomoolmarket.common.constants.ExceptionConstants;
import com.woomoolmarket.common.util.CookieUtils;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.security.jwt.factory.TokenFactory;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final TokenFactory tokenFactory;
  private final CacheService cacheService;
  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Value("${app.oauth2.authorizedRedirectUris}")
  private List<String> authorizedRedirectUris;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth) throws IOException {
    String targetUrl = determineTargetUrl(req, resp, auth);
    if (resp.isCommitted()) {
      log.info("[WOOMOOL-INFO] :: Response has already been committed. Unable to redirect to {}", targetUrl);
      return;
    }

    sendTokenToClient(resp, auth);
    saveAuthToContextHolder(auth);

    clearAuthenticationAttributes(req, resp);
    getRedirectStrategy().sendRedirect(req, resp, targetUrl);
  }

  private void saveAuthToContextHolder(Authentication oAuth2AuthenticationToken) {
    SecurityContextHolder.getContext().setAuthentication(oAuth2AuthenticationToken);
  }

  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    Optional<String> redirectUri = CookieUtils
      .getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
      .map(Cookie::getValue);

    if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
      throw new AccessDeniedException(ExceptionConstants.UNAUTHORIZED_URI);
    }

    return redirectUri.orElseGet(this::getDefaultTargetUrl);
  }

  private void sendTokenToClient(HttpServletResponse resp, Authentication auth) {
    TokenResponse tokenResponse = tokenFactory.createToken(auth);

    String username = auth.getName();
    String accessToken = tokenResponse.getAccessToken();
    String refreshToken = tokenResponse.getRefreshToken();
    saveTokensToRedis(accessToken, refreshToken, username);

    CookieUtils.addCookie(resp, REFRESH_TOKEN, refreshToken, REFRESH_TOKEN_EXPIRE_SECONDS);
    resp.setHeader(AUTHORIZATION_HEADER, accessToken);
  }

  private void saveTokensToRedis(String accessToken, String refreshToken, String username) {
    cacheService.setDataAndExpiration(LOGIN_ACCESS_TOKEN_PREFIX + username, accessToken, ACCESS_TOKEN_EXPIRE_SECONDS);
    cacheService.setDataAndExpiration(LOGIN_REFRESH_TOKEN_PREFIX + username, refreshToken, REFRESH_TOKEN_EXPIRE_SECONDS);
  }

  protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
  }

  private boolean isAuthorizedRedirectUri(String uri) {
    URI clientRedirectUri = URI.create(uri);

    return authorizedRedirectUris
      .stream()
      .anyMatch(authorizedRedirectUri -> {
        URI authorizedURI = URI.create(authorizedRedirectUri);
        return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
          && authorizedURI.getPort() == clientRedirectUri.getPort();
      });
  }
}
