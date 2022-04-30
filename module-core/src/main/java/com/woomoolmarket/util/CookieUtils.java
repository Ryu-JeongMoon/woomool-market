package com.woomoolmarket.util;

import com.woomoolmarket.security.dto.OAuth2TokenResponse;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.util.constants.Times;
import com.woomoolmarket.util.constants.TokenConstants;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.SerializationUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtils {

  public static void addLocalTokenToBrowser(HttpServletResponse response, TokenResponse tokenResponse) {
    String idToken = tokenResponse.getIdToken();
    if (StringUtils.isNotBlank(idToken)) {
      addCookie(
        response,
        TokenConstants.ID_TOKEN,
        idToken,
        Times.COOKIE_EXPIRATION_SECONDS.getValue());
    }

    String accessToken = tokenResponse.getAccessToken();
    if (StringUtils.isNotBlank(accessToken)) {
      addCookie(
        response,
        TokenConstants.ACCESS_TOKEN,
        TokenConstants.BEARER_TYPE + accessToken,
        Times.COOKIE_EXPIRATION_SECONDS.getValue());
    }
  }

  public static void addOAuth2TokenToBrowser(HttpServletResponse response, OAuth2TokenResponse renewedTokenResponse) {
    String accessToken = renewedTokenResponse.getAccessToken();
    if (StringUtils.isNotBlank(accessToken)) {
      addCookie(
        response,
        TokenConstants.ACCESS_TOKEN,
        TokenConstants.BEARER_TYPE + accessToken,
        Times.COOKIE_EXPIRATION_SECONDS.getValue());
    }

    String oidcIdToken = renewedTokenResponse.getOidcIdToken();
    if (StringUtils.isNotBlank(oidcIdToken)) {
      addCookie(
        response,
        TokenConstants.ID_TOKEN,
        oidcIdToken,
        Times.COOKIE_EXPIRATION_SECONDS.getValue());
    }
  }

  public static void clearTokenFromBrowser(HttpServletRequest request, HttpServletResponse response) {
    deleteCookie(request, response, TokenConstants.ID_TOKEN);
    deleteCookie(request, response, TokenConstants.ACCESS_TOKEN);
  }

  public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null || cookies.length == 0)
      return Optional.empty();

    return Arrays.stream(cookies)
      .filter(cookie -> StringUtils.equals(cookie.getName(), name))
      .findAny();
  }

  public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null || cookies.length == 0)
      return;

    Arrays.stream(cookies)
      .filter(cookie -> StringUtils.equals(cookie.getName(), name))
      .forEach(cookie -> {
        cookie.setValue("");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
      });
  }

  public static String serialize(Object object) {
    return Base64.getUrlEncoder()
      .encodeToString(SerializationUtils.serialize(object));
  }

  public static <T> T deserialize(Cookie cookie, Class<T> clazz) {
    return clazz.cast(SerializationUtils.deserialize(
      Base64.getUrlDecoder().decode(cookie.getValue())));
  }
}