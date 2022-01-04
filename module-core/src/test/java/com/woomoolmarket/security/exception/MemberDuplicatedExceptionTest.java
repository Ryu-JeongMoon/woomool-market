package com.woomoolmarket.security.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberDuplicatedExceptionTest {

  @Test
  void duplicateException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      throw new IllegalArgumentException("panda");
    });
  }
}