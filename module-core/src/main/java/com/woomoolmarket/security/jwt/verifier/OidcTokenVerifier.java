package com.woomoolmarket.security.jwt.verifier;

public interface OidcTokenVerifier extends OAuth2TokenVerifier {

  @Override
  default boolean isOAuth2Token(String idToken) {
    return false;
  }

  boolean isOidcToken(String idToken);
}
