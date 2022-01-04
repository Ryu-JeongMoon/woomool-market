package com.woomoolmarket.security.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TokenRequestTest {

  @Test
  void getSetTest() {
    TokenRequest tokenRequest = TokenRequest.builder()
      .accessToken("panda")
      .refreshToken("bear")
      .build();

    assertThat(tokenRequest.getAccessToken()).isEqualTo("panda");
    assertThat(tokenRequest.getRefreshToken()).isEqualTo("bear");
  }
}