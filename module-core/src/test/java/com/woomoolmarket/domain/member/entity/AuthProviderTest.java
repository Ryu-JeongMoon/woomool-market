package com.woomoolmarket.domain.member.entity;

import static com.woomoolmarket.domain.member.entity.AuthProvider.GOOGLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
class AuthProviderTest {

  @Test
  @DisplayName("Enum Type -> String 형태로 변환된다")
  void enumTest() {
    assertEquals(GOOGLE.toString(), "GOOGLE");
  }

  @Test
  @DisplayName("소문자로는 안 된당")
  void enumSmallLetterTest() {
    assertNotEquals(GOOGLE.toString(), "google");
  }
}