package com.woomoolmarket.security.jwt.verifier;

public interface OAuth2TokenVerifier extends TokenVerifier {

  @Override
  default boolean isLocalToken(String idToken) {
    return false;
  }

  boolean isOAuth2Token(String idToken);
}
