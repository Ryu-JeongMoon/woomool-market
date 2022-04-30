package com.woomoolmarket.security.jwt.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.nimbusds.jose.JWSAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Hmac512TokenFactoryTest {

  @Test
  @DisplayName("")
  void compareAlgorithm() {
    assertThat(JWSAlgorithm.HS512.getName()).isEqualTo("HS512");
  }
}