package com.woomoolmarket.util.constant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardConstantsTest {

  @Test
  @DisplayName("올바른 문자열 반환")
  void constantModify() {
    Assertions.assertThat(BoardConstants.MODIFY).isEqualTo("modify-board");
  }

  @Test
  @DisplayName("올바른 문자열 반환")
  void constantDelete() {
    Assertions.assertThat(BoardConstants.DELETE).isEqualTo("delete-board");
  }
}