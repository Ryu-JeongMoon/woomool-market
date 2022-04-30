package com.woomoolmarket.security.jwt.verifier;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jwt.SignedJWT;
import com.woomoolmarket.domain.port.CacheTokenPort;
import com.woomoolmarket.util.TokenUtils;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class EllipticCurveVerifier implements ApplicationTokenVerifier {

  private final Ed25519Verifier ed25519Verifier;
  private final CacheTokenPort cacheTokenPort;

  @Override
  public boolean isValid(String token) {
    try {
      SignedJWT signedJWT = getSingedJWT(token);
      return signedJWT.verify(ed25519Verifier)
        && TokenUtils.isNonBlocked(token, cacheTokenPort);
    } catch (ParseException | JOSEException e) {
      return false;
    }
  }
}
