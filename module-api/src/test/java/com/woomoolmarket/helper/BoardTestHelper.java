package com.woomoolmarket.helper;

import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.member.entity.Member;
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
            .build();
        return boardRepository.save(board);
    }
}
