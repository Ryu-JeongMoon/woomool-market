package com.woomoolmarket.service.board;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.dto.request.BoardModifyRequest;
import com.woomoolmarket.domain.board.dto.request.BoardRequest;
import com.woomoolmarket.domain.board.dto.response.BoardResponse;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.board.repository.BoardSearchCondition;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.board.mapper.BoardResponseMapper;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Transactional
@SpringBootTest
class BoardServiceTest {

    private final String MEMBER_1_EMAIL = "panda@naver.com";
    private final String MEMBER_2_EMAIL = "tiger@naver.com";
    private final String BOARD_1_TITLE = "panda1";
    private final String BOARD_1_CONTENT = "bear1";
    private final String BOARD_2_TITLE = "panda2";
    private final String BOARD_2_CONTENT = "bear2";
    private final String BOARD_3_TITLE = "panda3";
    private final String BOARD_3_CONTENT = "bear3";
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardService boardService;
    @Autowired
    BoardResponseMapper boardResponseMapper;
    @Autowired
    EntityManager em;
    private Long BOARD_1_ID;
    private Long BOARD_2_ID;
    private Long BOARD_3_ID;
    private Member member1;
    private Member member2;

    @BeforeEach
    void init() {
        boardRepository.deleteAll();
        em.createNativeQuery("ALTER TABLE BOARD ALTER COLUMN `board_id` RESTART WITH 1").executeUpdate();

        member1 = Member.builder()
            .email(MEMBER_1_EMAIL)
            .nickname("bear")
            .password("123456")
            .build();

        member2 = Member.builder()
            .email(MEMBER_2_EMAIL)
            .nickname("cat")
            .password("123456")
            .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        em.flush();

        Board board1 = Board.builder()
            .member(member1)
            .title(BOARD_1_TITLE)
            .content(BOARD_1_CONTENT)
            .boardCategory(BoardCategory.QNA)
            .startDateTime(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
            .endDateTime(LocalDateTime.of(2099, 1, 1, 1, 1, 1))
            .build();

        Board board2 = Board.builder()
            .member(member1)
            .title(BOARD_2_TITLE)
            .content(BOARD_2_CONTENT)
            .boardCategory(BoardCategory.FREE)
            .startDateTime(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
            .endDateTime(LocalDateTime.of(2099, 1, 1, 1, 1, 1))
            .build();

        Board board3 = Board.builder()
            .member(member2)
            .title(BOARD_3_TITLE)
            .content(BOARD_3_CONTENT)
            .boardCategory(BoardCategory.NOTICE)
            .startDateTime(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
            .endDateTime(LocalDateTime.of(2099, 1, 1, 1, 1, 1))
            .build();

        BOARD_1_ID = boardRepository.save(board1).getId();
        BOARD_2_ID = boardRepository.save(board2).getId();
        BOARD_3_ID = boardRepository.save(board3).getId();
    }

    @Test
    @DisplayName("검색 조건 생성하지 않을 시 전체 검색")
    void getListBySearchCondition() {
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition();
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<BoardResponse> boardResponses =
            boardService.getListBySearchCondition(boardSearchCondition, pageRequest);

        assertThat(boardResponses.getTotalElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("Admin 전용 검색")
    void getListBySearchConditionForAdmin() {
        BoardSearchCondition searchCondition1 = new BoardSearchCondition();
        List<BoardResponse> boardResponses1 = boardService.getListBySearchConditionForAdmin(searchCondition1);

        BoardSearchCondition searchCondition2 = BoardSearchCondition.builder().status(Status.INACTIVE).build();
        List<BoardResponse> boardResponses2 = boardService.getListBySearchConditionForAdmin(searchCondition2);

        assertThat(boardResponses1.size()).isEqualTo(3);
        assertThat(boardResponses2.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("fetch join 결과")
    void getListBySearchConditionForAdmin2() {
        BoardSearchCondition condition = BoardSearchCondition.builder().email("tiger").build();
        List<BoardResponse> boardResponses = boardService.getListBySearchConditionForAdmin(condition);
        assertThat(boardResponses.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("ID & STATUS 조건 검색")
    void getByIdAndStatus() {
        BoardResponse boardResponse = boardService.getByIdAndStatus(BOARD_1_ID, Status.ACTIVE);
        assertThat(boardResponse.getTitle()).isEqualTo(BOARD_1_TITLE);
    }

    @Test
    @DisplayName("조회수 증가")
    void increaseHit() {
        boardService.increaseHit(1L);
        Board board = boardRepository.findById(1L).get();
        assertThat(board.getHit()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 등록")
    void register() {
        BoardRequest boardRequest = BoardRequest.builder()
            .email(MEMBER_1_EMAIL)
            .title("hello")
            .content("hi")
            .build();
        boardService.register(boardRequest, null);
        assertThat(boardRepository.findById(4L).get()).isNotNull();
    }

    @Test
    @DisplayName("게시글 수정")
    void edit() {
        BoardModifyRequest modifyRequest = BoardModifyRequest.builder()
            .title("hello")
            .boardCategory(BoardCategory.FREE)
            .build();

        BoardResponse boardResponse = boardService.edit(1L, modifyRequest);

        assertThat(boardResponse.getTitle()).isEqualTo("hello");
        assertThat(boardResponse.getBoardCategory()).isEqualTo(BoardCategory.FREE);
    }

    @Test
    @DisplayName("게시글 soft 삭제")
    void deleteSoftly() {
        boardService.deleteSoftly(2L);
        Board board = boardRepository.findById(2L).get();

        assertThat(board.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(board.getDeletedDateTime()).isNotNull();
    }

    @Test
    @DisplayName("삭제된 게시글 복구")
    void restore() {
        boardService.deleteSoftly(2L);
        boardService.restore(2L);

        Board board = boardRepository.findById(2L).get();

        assertThat(board.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(board.getDeletedDateTime()).isNull();
    }
}