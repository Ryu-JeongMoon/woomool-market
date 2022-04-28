package com.woomoolmarket.security.oauth2;


import static com.woomoolmarket.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.woomoolmarket.util.CookieUtils;
import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
    throws IOException {
    log.info("[WOOMOOL-FAILED] :: Can't Login By OAuth2 => {}", exception.getMessage());

    String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
      .map(Cookie::getValue)
      .orElseGet(() -> "/");

    targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
      .queryParam("error", exception.getLocalizedMessage())
      .build()
      .toUriString();

    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}