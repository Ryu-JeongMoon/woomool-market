package com.woomoolmarket.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.AbstractDataJpaTest;
import com.woomoolmarket.domain.entity.Board;
import com.woomoolmarket.domain.entity.enumeration.Status;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class BoardRepositoryTest extends AbstractDataJpaTest {

  @Test
  @DisplayName("Board 생성 시 기본으로 ACTIVE 들어간다")
  void signUpTest() {
    Board board = Board.builder()
      .title("panda")
      .content("bear")
      .build();

    assertThat(board.getStatus()).isEqualTo(Status.ACTIVE);
  }
}
