package com.woomoolmarket.domain.token.entiy;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RefreshTokenTest {

    @Test
    @DisplayName("refresh-token 생성된다")
    void createToken() {
        RefreshToken refreshToken = RefreshToken.builder()
            .key("panda")
            .value("bear")
            .build();

        assertThat(refreshToken.getKey()).isEqualTo("panda");
        assertThat(refreshToken.getValue()).isEqualTo("bear");
    }

    @Test
    @DisplayName("refresh-token 수정된다")
    void updateTest() {
        RefreshToken refreshToken = RefreshToken.builder()
            .key("panda")
            .value("bear")
            .build();

        refreshToken.updateValue("BEAR");

        assertThat(refreshToken.getValue()).isNotEqualTo("bear");
        assertThat(refreshToken.getValue()).isEqualTo("BEAR");
    }
}