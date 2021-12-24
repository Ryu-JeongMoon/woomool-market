package com.woomoolmarket.common.constants;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class CacheConstantsTest {

    @Test
    @DisplayName("올바른 Cache 상수 값 반환")
    void constantsTest() {
        assertThat(CacheConstants.LOGIN_KEY_PREFIX).isEqualTo("auth:login#");
        assertThat(CacheConstants.LOGOUT_KEY_PREFIX).isEqualTo("auth:logout#");
        assertThat(CacheConstants.LOGIN_FAILED_KEY_PREFIX).isEqualTo("auth:login-failed#");
    }
}