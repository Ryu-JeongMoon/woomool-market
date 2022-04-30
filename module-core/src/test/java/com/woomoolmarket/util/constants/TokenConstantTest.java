package com.woomoolmarket.util.constants;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenConstantTest {

  @Test
  @DisplayName("올바른 Token 상수 값 반환")
  void constantTest() {
    assertThat(TokenConstants.BEARER_TYPE).isEqualTo("Bearer ");
    assertThat(TokenConstants.AUTHORITIES).isEqualTo("authorities");
    assertThat(TokenConstants.AUTHORIZATION_HEADER).isEqualTo("Authorization");
  }
}