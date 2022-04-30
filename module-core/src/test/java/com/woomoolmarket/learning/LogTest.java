package com.woomoolmarket.learning;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class LogTest {

  @Test
  @DisplayName("")
  void logging() {
    log.info("hi");
  }
}
