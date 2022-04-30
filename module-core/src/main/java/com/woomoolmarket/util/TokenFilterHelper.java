package com.woomoolmarket.util;

import com.woomoolmarket.domain.port.RefreshTokenPort;
import com.woomoolmarket.domain.repository.querydto.RefreshTokenQueryResponse;
import com.woomoolmarket.security.dto.UserPrincipal;
import com.woomoolmarket.security.jwt.verifier.TokenVerifier;
import org.springframework.stereotype.Component;

@Component
public record TokenFilterHelper(RefreshTokenPort refreshTokenPort) {

  public String getRefreshTokenByIdToken(TokenVerifier tokenVerifier, String idToken) {
    UserPrincipal principal = (UserPrincipal) tokenVerifier
      .getAuthentication(idToken)
      .getPrincipal();

    RefreshTokenQueryResponse refreshTokenQueryResponse = refreshTokenPort.findByEmail(principal.getEmail());
    return refreshTokenQueryResponse.getTokenValue();
  }

  public boolean setAuthenticationIfValid(TokenVerifier tokenVerifier, String idToken) {
    boolean isValid = tokenVerifier.isValid(idToken);
    if (isValid)
      SecurityUtils.setAuthentication(tokenVerifier.getAuthentication(idToken));

    return isValid;
  }
}
