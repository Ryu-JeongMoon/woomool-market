package com.woomoolmarket.config;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
class PasswordEncoderTest {

  PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

  @Test
  @DisplayName("인코딩 마다 다른 비밀번호 반환")
  void passwordTest() {
    String password = "1234";

    String encodedPassword = passwordEncoder.encode(password);
    String encodedPassword2 = passwordEncoder.encode(password);

    assertNotEquals(password, encodedPassword);
    assertNotEquals(encodedPassword, encodedPassword2);
    assertTrue(passwordEncoder.matches(password, encodedPassword));
  }

}