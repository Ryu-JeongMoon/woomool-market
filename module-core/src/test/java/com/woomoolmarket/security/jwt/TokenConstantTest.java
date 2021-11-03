package com.woomoolmarket.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenConstantTest {

    @Test
    @DisplayName("올바른 상수 값 반환")
    void constantTest() {
        assertThat(TokenConstant.LOGOUT_KEY_PREFIX).isEqualTo("logout:");
        assertThat(TokenConstant.BEARER_TYPE).isEqualTo("Bearer");
        assertThat(TokenConstant.AUTHORITIES_KEY).isEqualTo("auth");
        assertThat(TokenConstant.AUTHORIZATION_HEADER).isEqualTo("Authorization");
        assertThat(TokenConstant.ACCESS_TOKEN_EXPIRE_TIME).isEqualTo(1000 * 60 * 30);
        assertThat(TokenConstant.REFRESH_TOKEN_EXPIRE_TIME).isEqualTo(1000 * 60 * 60 * 24 * 7);
    }
}