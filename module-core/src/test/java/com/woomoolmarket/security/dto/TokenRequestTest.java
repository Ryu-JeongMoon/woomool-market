package com.woomoolmarket.security.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenRequestTest {

    @Test
    void getSetTest() {
        TokenRequest tokenRequest = TokenRequest.builder()
            .accessToken("panda")
            .refreshToken("bear")
            .build();

        assertThat(tokenRequest.getAccessToken()).isEqualTo("panda");
        assertThat(tokenRequest.getRefreshToken()).isEqualTo("bear");
    }
}