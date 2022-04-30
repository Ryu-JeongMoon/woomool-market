package com.woomoolmarket.security.jwt.verifier;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.woomoolmarket.domain.port.CacheTokenPort;
import com.woomoolmarket.security.jwt.properties.HmacProperties;
import com.woomoolmarket.util.DateTimeUtils;
import com.woomoolmarket.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(HmacProperties.class)
public class HmacVerifier implements ApplicationTokenVerifier {

  private final CacheTokenPort cacheTokenPort;

  @Override
  public boolean isValid(String token) {
    DecodedJWT decodedJWT = getDecodedJWT(token);
    return TokenUtils.isHmac512Algorithm(decodedJWT.getAlgorithm())
      && DateTimeUtils.isAfterThanNow(decodedJWT.getExpiresAt())
      && !TokenUtils.isNonBlocked(token, cacheTokenPort);
  }
}
