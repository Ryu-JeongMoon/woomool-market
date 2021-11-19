package com.woomoolmarket.domain.board.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.common.enumeration.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardSearchConditionTest {

    @Test
    @DisplayName("SearchCondition 생성 시 기본 상태 ACTIVE 로 초기화")
    void builderTest() {
        BoardSearchCondition c1 = BoardSearchCondition.builder()
            .status(Status.ACTIVE)
            .build();

        BoardSearchCondition c2 = BoardSearchCondition.builder()
            .build();

        assertThat(c1.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(c2.getStatus()).isEqualTo(Status.ACTIVE);
    }
}