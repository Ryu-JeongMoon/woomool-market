package com.woomoolmarket.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenConstantTest {

    @Test
    @DisplayName("올바른 상수 값 반환")
    void constantTest() {
        assertThat(TokenConstants.LOGOUT_KEY_PREFIX).isEqualTo("logout:");
        assertThat(TokenConstants.BEARER_TYPE).isEqualTo("Bearer ");
        assertThat(TokenConstants.AUTHORITIES_KEY).isEqualTo("auth");
        assertThat(TokenConstants.AUTHORIZATION_HEADER).isEqualTo("Authorization");
        assertThat(TokenConstants.ACCESS_TOKEN_EXPIRE_MILLIS).isEqualTo(1000 * 60 * 30);
        assertThat(TokenConstants.REFRESH_TOKEN_EXPIRE_MILLIS).isEqualTo(1000 * 60 * 60 * 24 * 7);
    }
}