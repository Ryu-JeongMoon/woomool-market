package com.woomoolmarket.util.constant;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartConstantsTest {

    @Test
    @DisplayName("올바른 문자열 반환 - cart-list")
    void constantList() {
        assertThat(CartConstants.LIST).isEqualTo("cart-list");
    }
}