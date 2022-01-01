package com.woomoolmarket.security.dto;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenResponseTest {

    private TokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        tokenResponse = TokenResponse.builder()
            .grantType("bearer")
            .accessToken("panda")
            .refreshToken("bear")
            .accessTokenExpiresIn(10101010)
            .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("getter 테스트")
    void getterTest() {
        assertThat(tokenResponse.getGrantType()).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotNull();
        assertThat(tokenResponse.getRefreshToken()).isNotNull();
        assertThat(tokenResponse.getAccessTokenExpiresIn()).isNotNull();
    }
}