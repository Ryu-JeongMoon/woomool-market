package com.woomoolmarket.security.jwt.factory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.woomoolmarket.security.jwt.properties.RSA512Properties;
import com.woomoolmarket.util.TokenUtils;
import com.woomoolmarket.util.constants.Times;
import com.woomoolmarket.util.constants.TokenConstants;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableConfigurationProperties(RSA512Properties.class)
public class RSA512TokenFactory extends TokenFactory {

  private final RSAPublicKey rsaPublicKey;
  private final RSAPrivateKey rsaPrivateKey;
  private final RsaSigner rsaSigner;

  public RSA512TokenFactory(RSA512Properties rsa512Properties) throws JOSEException {
    RSAKey rsaKey = new RSAKeyGenerator(4096)
      .keyUse(KeyUse.SIGNATURE)
      .keyID(rsa512Properties.privateKeyPlainText())
      .generate();

    rsaPublicKey = rsaKey.toRSAPublicKey();
    rsaPrivateKey = rsaKey.toRSAPrivateKey();
    rsaSigner = new RsaSigner(rsaPrivateKey);
  }

  @Bean
  public RSASSAVerifier rsaVerifier() {
    return new RSASSAVerifier(rsaPublicKey);
  }

  @Override
  protected String createIdToken(Authentication authentication) {
    return JWT.create()
      .withHeader(Map.of(TokenConstants.ALGORITHM, JWSAlgorithm.RS512))
      .withSubject(authentication.getName())
      .withIssuer(TokenConstants.LOCAL_TOKEN_ISSUER)
      .withClaim(TokenConstants.AUTHORITIES, TokenUtils.getAuthorities(authentication))
      .withClaim(TokenConstants.TOKEN_TYPE, TokenConstants.ID_TOKEN)
      .withClaim(TokenConstants.USER_ID, TokenUtils.getIdFromAuthentication(authentication))
      .withExpiresAt(createExpirationDate(Times.ID_TOKEN_EXPIRATION_SECONDS))
      .sign(Algorithm.RSA512(rsaPublicKey, rsaPrivateKey));
  }

  @Override
  protected String createRefreshToken(Authentication authentication) {
    return JWT.create()
      .withSubject(authentication.getName())
      .withClaim(TokenConstants.AUTHORITIES, TokenUtils.getAuthorities(authentication))
      .withClaim(TokenConstants.USER_ID, TokenUtils.getIdFromAuthentication(authentication))
      .withExpiresAt(createExpirationDate(Times.REFRESH_TOKEN_EXPIRATION_SECONDS))
      .sign(Algorithm.RSA512(rsaPublicKey, rsaPrivateKey));
  }
}
