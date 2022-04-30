package com.woomoolmarket.security.oauth2;

import com.woomoolmarket.util.CookieUtils;
import com.woomoolmarket.util.constants.TokenConstants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

  private static final int COOKIE_EXPIRE_SECONDS = 180;

  @Override
  public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
    return CookieUtils.getCookie(request, TokenConstants.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
      .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
      .orElseGet(() -> null);
  }

  @Override
  public void saveAuthorizationRequest(OAuth2AuthorizationRequest authRequest, HttpServletRequest request, HttpServletResponse response) {
    if (authRequest == null)
      return;

    CookieUtils.addCookie(response, TokenConstants.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authRequest), COOKIE_EXPIRE_SECONDS);

    String redirectUriAfterLogin = request.getParameter(TokenConstants.REDIRECT_URI);
    if (StringUtils.hasText(redirectUriAfterLogin))
      CookieUtils.addCookie(response, TokenConstants.REDIRECT_URI, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
  }

  @Override
  public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
    return this.loadAuthorizationRequest(request);
  }

  public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
    CookieUtils.deleteCookie(request, response, TokenConstants.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    CookieUtils.deleteCookie(request, response, TokenConstants.REDIRECT_URI);
  }
}