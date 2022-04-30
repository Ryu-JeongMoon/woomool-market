package com.woomoolmarket.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.AbstractDataJpaTest;
import com.woomoolmarket.domain.entity.Board;
import com.woomoolmarket.domain.entity.QBoard;
import com.woomoolmarket.domain.entity.enumeration.BoardCategory;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class BoardRepositoryImplTest extends AbstractDataJpaTest {

  @BeforeEach
  void init() {
    boardRepository.deleteAll();
    entityManager.createNativeQuery("ALTER TABLE BOARD ALTER COLUMN `board_id` RESTART WITH 1").executeUpdate();

    Board board = Board.builder()
      .title("panda1")
      .content("bear1")
      .boardCategory(BoardCategory.QNA)
      .startDateTime(LocalDateTime.now())
      .endDateTime(LocalDateTime.of(2099, 1, 1, 1, 1, 1))
      .build();

    boardRepository.save(board);
  }

  @Test
  void containsTest() {
    Board board = queryFactory.selectFrom(QBoard.board)
      .where(QBoard.board.title.contains("pa"))
      .fetchOne();

    assertThat(board).isNotNull();
  }
}