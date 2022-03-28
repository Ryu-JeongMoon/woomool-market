package com.woomoolmarket.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CoolSmsPropertiesTest {

  @Autowired
  CoolSmsProperties coolSmsProperties;

  @Test
  @DisplayName("properties binding 성공")
  void coolSmsProperties() {
    assertAll(
      () -> assertThat(coolSmsProperties.getEmail()).isNotBlank(),
      () -> assertThat(coolSmsProperties.getApiKey()).isNotBlank(),
      () -> assertThat(coolSmsProperties.getApiSecret()).isNotBlank(),
      () -> assertThat(coolSmsProperties.getPhoneNumber()).isNotBlank()
    );
  }
}