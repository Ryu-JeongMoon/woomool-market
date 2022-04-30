package com.woomoolmarket.helper;

import com.woomoolmarket.domain.entity.Board;
import com.woomoolmarket.domain.entity.enumeration.BoardCategory;
import com.woomoolmarket.domain.repository.BoardRepository;
import com.woomoolmarket.domain.entity.Member;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardTestHelper {

  public static final String BOARD_TITLE = "panda is cool";
  public static final String BOARD_CONTENT = "yahoo";

  private final BoardRepository boardRepository;

  public Board createBoard(Member member) {
    Board board = Board.builder()
      .member(member)
      .title(BOARD_TITLE)
      .content(BOARD_CONTENT)
      .boardCategory(BoardCategory.NOTICE)
      .startDateTime(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
      .endDateTime(LocalDateTime.of(2099, 1, 1, 1, 1, 1))
      .build();
    return boardRepository.save(board);
  }

  public Board createCustomBoard(Member member, String title, String content, BoardCategory boardCategory) {
    Board board = Board.builder()
      .member(member)
      .title(title)
      .content(content)
      .boardCategory(boardCategory)
      .startDateTime(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
      .endDateTime(LocalDateTime.of(2099, 1, 1, 1, 1, 1))
      .build();
    return boardRepository.save(board);
  }
}
