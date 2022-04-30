package com.woomoolmarket.helper;

import com.woomoolmarket.domain.entity.Board;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.enumeration.BoardCategory;
import java.time.LocalDateTime;

public class BoardTestHelper {

  public static final String BOARD_TITLE = "panda is cool";
  public static final String BOARD_CONTENT = "yahoo";

  public static Board createBoard(Member member) {
    return Board.builder()
      .member(member)
      .title(BOARD_TITLE)
      .content(BOARD_CONTENT)
      .boardCategory(BoardCategory.NOTICE)
      .startDateTime(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
      .endDateTime(LocalDateTime.of(2099, 1, 1, 1, 1, 1))
      .build();
  }
}
