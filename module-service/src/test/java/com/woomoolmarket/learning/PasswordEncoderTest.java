package com.woomoolmarket.learning;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class PasswordEncoderTest {

  private final PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

  @Test
  @DisplayName("")
  void encode() {
    String password = "123456";
    String encodedPassword = passwordEncoder.encode(password);

    boolean matches = passwordEncoder.matches(password, encodedPassword);
    log.info("matches = {}",matches);
  }
}
