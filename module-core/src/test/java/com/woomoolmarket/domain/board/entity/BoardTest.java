package com.woomoolmarket.domain.board.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.domain.enumeration.Status;
import com.woomoolmarket.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class BoardTest {

  private final String BOARD_TITLE = "panda";
  private final String BOARD_CONTENT = "panda is bear";
  private final String MEMBER_EMAIL = "panda@naver.com";
  private final String MEMBER_PASSWORD = "123456";
  private Board board;

  @BeforeEach
  void init() {
    Member member = Member.builder()
      .email(MEMBER_EMAIL)
      .password(MEMBER_PASSWORD)
      .build();

    board = Board.builder()
      .title(BOARD_TITLE)
      .content(BOARD_CONTENT)
      .member(member)
      .boardCategory(BoardCategory.NOTICE)
      .build();
  }

  @Test
  @DisplayName("게시글 조회수 증가")
  void changeHit() {
    board.increaseHit();
    assertThat(board.getHit()).isEqualTo(1);
  }

  @Test
  @DisplayName("게시글 삭제 시 soft delete")
  void delete() {
    board.delete();
    assertThat(board.getStatus()).isEqualTo(Status.INACTIVE);
    assertThat(board.getDeletedDateTime()).isNotNull();
  }

  @Test
  @DisplayName("삭제된 게시글 복구")
  void restore() {
    board.delete();
    board.restore();
    assertThat(board.getStatus()).isEqualTo(Status.ACTIVE);
    assertThat(board.getDeletedDateTime()).isNull();
  }
}