package com.woomoolmarket.service.auth.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EmailRequestTest {

  @Test
  void getEmail() {
    EmailRequest emailRequest = EmailRequest.builder()
      .email("panda")
      .build();

    assertThat(emailRequest.getEmail()).isEqualTo("panda");
  }

  @Test
  void setEmail() {
    EmailRequest emailRequest = new EmailRequest();
    emailRequest.setEmail("bear");

    assertThat(emailRequest.getEmail()).isEqualTo("bear");
  }
}