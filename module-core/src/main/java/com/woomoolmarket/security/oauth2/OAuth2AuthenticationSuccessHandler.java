package com.woomoolmarket.security.oauth2;

import static com.woomoolmarket.util.constants.CacheConstants.LOGIN_ACCESS_TOKEN_PREFIX;
import static com.woomoolmarket.util.constants.CacheConstants.LOGIN_REFRESH_TOKEN_PREFIX;

import com.woomoolmarket.domain.entity.enumeration.Role;
import com.woomoolmarket.domain.port.CacheTokenPort;
import com.woomoolmarket.security.dto.OAuth2TokenResponse;
import com.woomoolmarket.security.dto.OAuth2UserPrincipal;
import com.woomoolmarket.security.dto.UserPrincipal;
import com.woomoolmarket.util.CookieUtils;
import com.woomoolmarket.util.SecurityUtils;
import com.woomoolmarket.util.constants.Times;
import com.woomoolmarket.util.constants.UriConstants;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final CacheTokenPort cacheTokenPort;
  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

    OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();
    OAuth2TokenResponse oAuth2TokenResponse = OAuth2TokenResponse.builder()
      .accessToken(principal.getOAuth2Token().getTokenValue())
      .oidcIdToken(principal.getIdToken().getTokenValue())
      .build();

    SecurityUtils.setAuthentication(authentication);
    CookieUtils.addOAuth2TokenToBrowser(response, oAuth2TokenResponse);

    String targetUri = getUriByRole(principal);
    response.sendRedirect(targetUri);
  }

  private String getUriByRole(UserPrincipal principal) {
    return isUser(principal)
      ? UriConstants.Mapping.POSTS
      : UriConstants.Mapping.MEMBERS;
  }

  private boolean isUser(UserPrincipal principal) {
    return principal.getAuthorities()
      .stream()
      .anyMatch(Role.USER::equals);
  }

  private void saveTokensToRedis(String accessToken, String refreshToken, String username) {
    cacheTokenPort.setDataAndExpiration(LOGIN_ACCESS_TOKEN_PREFIX + username, accessToken, Times.ACCESS_TOKEN_EXPIRATION_SECONDS.getValue());
    cacheTokenPort.setDataAndExpiration(LOGIN_REFRESH_TOKEN_PREFIX + username, refreshToken, Times.REFRESH_TOKEN_EXPIRATION_SECONDS.getValue());
  }
}
