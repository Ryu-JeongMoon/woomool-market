package com.woomoolmarket.domain.board.query;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.config.DataJpaTestConfig;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.board.repository.BoardSearchCondition;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.helper.BoardTestHelper;
import com.woomoolmarket.helper.MemberTestHelper;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class BoardQueryResponseTest extends DataJpaTestConfig {

    @BeforeEach
    void setUp() {
        Member member = MemberTestHelper.createUser();
        memberRepository.save(member);

        Board board = BoardTestHelper.createBoard(member);
        boardRepository.save(board);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("회원 전용 검색 - 카테고리")
    void searchBy() {
        BoardSearchCondition condition = BoardSearchCondition.builder()
            .boardCategory(BoardCategory.NOTICE)
            .build();
        Page<BoardQueryResponse> page = boardRepository.searchBy(condition, Pageable.ofSize(10));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("회원 전용 검색 - 회원 상태 검색 불가")
    void searchByStatusNoResult() {
        BoardSearchCondition condition = BoardSearchCondition.builder()
            .status(Status.INACTIVE)
            .build();
        Page<BoardQueryResponse> page = boardRepository.searchBy(condition, Pageable.ofSize(10));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("관리자 전용 검색 - 상태 검색 가능")
    void searchForAdminBy() {
        BoardSearchCondition condition = BoardSearchCondition.builder()
            .status(Status.INACTIVE)
            .build();
        Page<BoardQueryResponse> page = boardRepository.searchForAdminBy(condition, Pageable.ofSize(10));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("관리자 전용 검색 - 게시글 활성일 지나도 검색 가능")
    void searchForAdminByStatus() {
        Board board = Board.builder()
            .title("PANDA")
            .content("BEAR")
            .boardCategory(BoardCategory.FREE)
            .startDateTime(LocalDateTime.of(1999, 1, 1, 1, 1, 1))
            .endDateTime(LocalDateTime.of(1999, 12, 31, 1, 1, 1))
            .build();
        boardRepository.save(board);

        BoardSearchCondition condition = BoardSearchCondition.builder()
            .boardCategory(BoardCategory.FREE)
            .build();
        Page<BoardQueryResponse> page = boardRepository.searchForAdminBy(condition, Pageable.ofSize(10));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(1);
    }
}