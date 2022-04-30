package com.woomoolmarket.util.webclient;

import com.woomoolmarket.security.dto.OAuth2TokenRequest;
import com.woomoolmarket.security.dto.OAuth2TokenResponse;

public interface OidcWebClientWrapper {

  boolean validateByOidc(String idToken);

  OAuth2TokenResponse getToken(OAuth2TokenRequest oAuth2TokenRequest);

  OAuth2TokenResponse getRenewedToken(OAuth2TokenRequest oAuth2TokenRequest);
}
