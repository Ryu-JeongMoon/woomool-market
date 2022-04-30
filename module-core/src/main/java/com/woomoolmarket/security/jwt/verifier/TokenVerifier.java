package com.woomoolmarket.security.jwt.verifier;

import org.springframework.security.core.Authentication;

public interface TokenVerifier {

  boolean isValid(String token);

  boolean isLocalToken(String idToken);

  Authentication getAuthentication(String idToken);
}
