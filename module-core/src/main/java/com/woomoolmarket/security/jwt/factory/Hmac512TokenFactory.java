package com.woomoolmarket.security.jwt.factory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.woomoolmarket.security.jwt.properties.HmacProperties;
import com.woomoolmarket.util.TokenUtils;
import com.woomoolmarket.util.constants.Times;
import com.woomoolmarket.util.constants.TokenConstants;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(HmacProperties.class)
public class Hmac512TokenFactory extends TokenFactory {

  private final HmacProperties hmacProperties;

  @Override
  protected String createIdToken(Authentication authentication) {
    return JWT.create()
      .withHeader(Map.of(TokenConstants.ALGORITHM, JWSAlgorithm.HS512.getName()))
      .withSubject(authentication.getName())
      .withIssuer(TokenConstants.LOCAL_TOKEN_ISSUER)
      .withClaim(TokenConstants.AUTHORITIES, TokenUtils.getAuthorities(authentication))
      .withClaim(TokenConstants.TOKEN_TYPE, TokenConstants.ID_TOKEN)
      .withClaim(TokenConstants.USER_ID, TokenUtils.getIdFromAuthentication(authentication))
      .withExpiresAt(createExpirationDate(Times.ID_TOKEN_EXPIRATION_SECONDS))
      .sign(Algorithm.HMAC512(hmacProperties.secret()));
  }

  @Override
  protected String createRefreshToken(Authentication authentication) {
    return JWT.create()
      .withSubject(authentication.getName())
      .withClaim(TokenConstants.AUTHORITIES, TokenUtils.getAuthorities(authentication))
      .withClaim(TokenConstants.USER_ID, TokenUtils.getIdFromAuthentication(authentication))
      .withExpiresAt(createExpirationDate(Times.REFRESH_TOKEN_EXPIRATION_SECONDS))
      .sign(Algorithm.HMAC512(hmacProperties.secret()));
  }
}
