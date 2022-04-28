package com.woomoolmarket.domain.board.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.domain.enumeration.Status;
import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.board.entity.Board;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Log4j2
@DataJpaTest
@Import(TestConfig.class)
public class BoardRepositoryTest {

  @Autowired
  BoardRepository boardRepository;

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
