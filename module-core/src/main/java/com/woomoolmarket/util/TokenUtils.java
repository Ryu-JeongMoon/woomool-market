package com.woomoolmarket.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.woomoolmarket.domain.entity.enumeration.Role;
import com.woomoolmarket.domain.port.CacheTokenPort;
import com.woomoolmarket.security.dto.UserPrincipal;
import com.woomoolmarket.util.constants.CacheConstants;
import com.woomoolmarket.util.constants.TokenConstants;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtils {

  private static final String EMPTY_VALUE = "";

  public static String getAuthorities(Authentication authentication) {
    return authentication.getAuthorities().stream()
      .map(grantedAuthority -> grantedAuthority.getAuthority().replace(TokenConstants.ROLE_PREFIX, ""))
      .findAny()
      .orElseGet(Role.USER::name);
  }

  public static Long getIdFromAuthentication(Authentication authentication) {
    Object principal = authentication.getPrincipal();
    if (!(principal instanceof UserPrincipal))
      return null;

    return ((UserPrincipal) principal).getId();
  }

  public static String resolveAccessToken(HttpServletRequest request) {
    return CookieUtils.getCookie(request, TokenConstants.ACCESS_TOKEN)
      .filter(cookie -> cookie.getValue().startsWith(TokenConstants.BEARER_TYPE))
      .map(cookie -> cookie.getValue().substring(TokenConstants.BEARER_TYPE.length()))
      .orElseGet(() -> EMPTY_VALUE);
  }

  public static String resolveIdToken(HttpServletRequest request) {
    return CookieUtils.getCookie(request, TokenConstants.ID_TOKEN)
      .map(Cookie::getValue)
      .orElseGet(() -> EMPTY_VALUE);
  }

  public static String resolveRefreshToken(HttpServletRequest request) {
    return CookieUtils.getCookie(request, TokenConstants.REFRESH_TOKEN)
      .map(Cookie::getValue)
      .orElseGet(() -> EMPTY_VALUE);
  }

  public static boolean isHmac512Algorithm(String algorithm) {
    return isSameAlgorithm(JWSAlgorithm.HS512.getName(), algorithm);
  }

  public static boolean isRsa512Algorithm(String algorithm) {
    return isSameAlgorithm(JWSAlgorithm.RS512.getName(), algorithm);
  }

  private static boolean isSameAlgorithm(String algorithm, String toBeCompareAlgorithm) {
    return StringUtils.equals(algorithm, toBeCompareAlgorithm);
  }

  public static SignedJWT getEmptySignedJWT() {
    return new SignedJWT(new JWSHeader(new JWSAlgorithm(JWSAlgorithm.NONE.getName())), new JWTClaimsSet.Builder().build());
  }

  public static DecodedJWT getEmptyDecodedJWT() {
    String emptyToken = JWT.create()
      .withHeader(Map.of(TokenConstants.ALGORITHM, JWSAlgorithm.NONE.getName()))
      .withClaim(TokenConstants.USER_ID, "")
      .withClaim(TokenConstants.EMAIL, "")
      .withClaim(TokenConstants.AUTHORITIES, "")
      .sign(Algorithm.HMAC512(""));

    return JWT.decode(emptyToken);
  }

  public static boolean isNonBlocked(String token, CacheTokenPort cacheTokenPort) {
    return StringUtils.isEmpty(cacheTokenPort.getData(CacheConstants.LOGOUT_KEY_PREFIX + token));
  }
}
