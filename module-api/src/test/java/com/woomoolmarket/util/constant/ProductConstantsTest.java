package com.woomoolmarket.util.constant;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductConstantsTest {

  @Test
  @DisplayName("올바른 문자열 반환 - modify-product")
  void constantModify() {
    assertThat(ProductConstants.MODIFY).isEqualTo("modify-product");
  }

  @Test
  @DisplayName("올바른 문자열 반환 - delete-product")
  void constantDelete() {
    assertThat(ProductConstants.DELETE).isEqualTo("delete-product");
  }
}