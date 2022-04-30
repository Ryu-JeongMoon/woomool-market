package com.woomoolmarket.learning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RsaSignatureTest {

  @Test
  @DisplayName("rsa key 생성 테스트")
  void keySetting() throws JOSEException, ParseException {
    RSAKey rsaJWK = new RSAKeyGenerator(2048)
      .keyID("123")
      .generate();
    RSAKey rsaPublicJWK = rsaJWK.toPublicJWK();

    // Create RSA-signer with the private key
    JWSSigner signer = new RSASSASigner(rsaJWK);

    // Prepare JWT with claims set
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
      .subject("alice")
      .issuer("https://c2id.com")
      .expirationTime(new Date(new Date().getTime() + 60 * 1000))
      .build();

    SignedJWT signedJWT = new SignedJWT(
      new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
      claimsSet);

    // Compute the RSA signature
    signedJWT.sign(signer);

    // To serialize to compact form, produces something like
    // eyJhbGciOiJSUzI1NiJ9.SW4gUlNBIHdlIHRydXN0IQ.IRMQENi4nJyp4er2L
    // mZq3ivwoAjqa1uUkSBKFIX7ATndFF5ivnt-m8uApHO4kfIFOrW7w2Ezmlg3Qd
    // maXlS9DhN0nUk_hGI3amEjkKd0BWYCB8vfUbUv0XGjQip78AI4z1PrFRNidm7
    // -jPDm5Iq0SZnjKjCNS5Q15fokXZc8u0A
    String s = signedJWT.serialize();

    // On the consumer side, parse the JWS and verify its RSA signature
    signedJWT = SignedJWT.parse(s);

    JWSVerifier verifier = new RSASSAVerifier(rsaPublicJWK);
    assertTrue(signedJWT.verify(verifier));

    // Retrieve / verify the JWT claims according to the app requirements
    assertEquals("alice", signedJWT.getJWTClaimsSet().getSubject());
    assertEquals("https://c2id.com", signedJWT.getJWTClaimsSet().getIssuer());
    assertTrue(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()));
  }
}
