package com.woomoolmarket.security.oauth2;


import com.woomoolmarket.util.CookieUtils;
import com.woomoolmarket.util.constants.TokenConstants;
import com.woomoolmarket.util.constants.UriConstants;
import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Override
  public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException {
    log.info("[WOOMOOL-FAILED] :: Can't Login By OAuth2 => {}", e.getMessage());

    String targetUrl = CookieUtils.getCookie(req, TokenConstants.REDIRECT_URI)
      .map(Cookie::getValue)
      .orElseGet(() -> UriConstants.Mapping.ROOT);

    targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
      .queryParam("error", e.getLocalizedMessage())
      .build()
      .toUriString();

    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(req, resp);
    getRedirectStrategy().sendRedirect(req, resp, targetUrl);
  }
}