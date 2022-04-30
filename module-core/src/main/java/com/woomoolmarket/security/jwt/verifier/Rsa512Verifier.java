package com.woomoolmarket.security.jwt.verifier;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.woomoolmarket.domain.port.CacheTokenPort;
import com.woomoolmarket.util.TokenUtils;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Rsa512Verifier implements ApplicationTokenVerifier {

  private final RSASSAVerifier rsaVerifier;
  private final CacheTokenPort cacheTokenPort;

  @Override
  public boolean isValid(String token) {
    try {
      SignedJWT signedJWT = getSingedJWT(token);
      return signedJWT.verify(rsaVerifier)
        && TokenUtils.isNonBlocked(token, cacheTokenPort);
    } catch (ParseException | JOSEException e) {
      return false;
    }
  }
}
