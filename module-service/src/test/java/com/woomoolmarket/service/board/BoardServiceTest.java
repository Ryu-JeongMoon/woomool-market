package com.woomoolmarket.service.board;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.AbstractServiceTest;
import com.woomoolmarket.domain.entity.Board;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.enumeration.BoardCategory;
import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.repository.querydto.BoardSearchCondition;
import com.woomoolmarket.domain.repository.querydto.BoardQueryResponse;
import com.woomoolmarket.service.board.dto.request.BoardModifyRequest;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import com.woomoolmarket.service.board.dto.response.BoardResponse;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
class BoardServiceTest extends AbstractServiceTest {

  private static Long BOARD_1_ID;
  private static Long BOARD_2_ID;
  private static Long BOARD_3_ID;
  private static String MEMBER_1_EMAIL;
  private static String MEMBER_2_EMAIL;

  private final String BOARD_1_TITLE = "panda1";
  private final String BOARD_1_CONTENT = "bear1";
  private final String BOARD_2_TITLE = "panda2";
  private final String BOARD_2_CONTENT = "bear2";
  private final String BOARD_3_TITLE = "panda3";
  private final String BOARD_3_CONTENT = "bear3";

  @BeforeEach
  void init() {
    Member member1 = memberTestHelper.createMember();
    Member member2 = memberTestHelper.createSeller();
    MEMBER_1_EMAIL = member1.getEmail();
    MEMBER_2_EMAIL = member2.getEmail();

    Board board1 = boardTestHelper.createCustomBoard(member1, BOARD_1_TITLE, BOARD_1_CONTENT, BoardCategory.QNA);
    Board board2 = boardTestHelper.createCustomBoard(member1, BOARD_2_TITLE, BOARD_2_CONTENT, BoardCategory.FREE);
    Board board3 = boardTestHelper.createCustomBoard(member2, BOARD_3_TITLE, BOARD_3_CONTENT, BoardCategory.NOTICE);
    BOARD_1_ID = board1.getId();
    BOARD_2_ID = board2.getId();
    BOARD_3_ID = board3.getId();
  }

  @AfterEach
  void clear() {
    em.createNativeQuery("ALTER TABLE BOARD ALTER COLUMN `board_id` RESTART WITH 1").executeUpdate();
    em.createNativeQuery("ALTER TABLE MEMBER ALTER COLUMN `member_id` RESTART WITH 1").executeUpdate();

    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @DisplayName("?????? ?????? ???????????? ?????? ??? ?????? ??????")
  void getListBySearchCondition() {
    BoardSearchCondition boardSearchCondition = new BoardSearchCondition();
    PageRequest pageRequest = PageRequest.of(0, 10);

    Page<BoardQueryResponse> boardResponses =
      boardService.searchBy(boardSearchCondition, pageRequest);

    assertThat(boardResponses.getTotalElements()).isEqualTo(3);
  }

  @Test
  @DisplayName("Admin ?????? ??????")
  void getListBySearchConditionForAdmin() {
    BoardSearchCondition searchCondition1 = new BoardSearchCondition();
    Page<BoardQueryResponse> page1 = boardService.searchForAdminBy(searchCondition1, Pageable.ofSize(10));

    BoardSearchCondition searchCondition2 = BoardSearchCondition.builder().status(Status.INACTIVE).build();
    Page<BoardQueryResponse> page2 = boardService.searchForAdminBy(searchCondition2, Pageable.ofSize(10));

    assertThat(page1.getTotalElements()).isEqualTo(3);
    assertThat(page2.getTotalElements()).isEqualTo(0);
  }

  @Test
  @DisplayName("fetch join ??????")
  void getListBySearchConditionForAdmin2() {
    BoardSearchCondition condition = BoardSearchCondition.builder()
      .email("bear")
      .build();
    Page<BoardQueryResponse> page = boardService.searchForAdminBy(condition, Pageable.ofSize(10));
    assertThat(page.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("ID & STATUS ?????? ??????")
  void getByIdAndStatus() {
    BoardResponse boardResponse = boardService.findBy(BOARD_1_ID, Status.ACTIVE);
    assertThat(boardResponse.getTitle()).isEqualTo(BOARD_1_TITLE);
  }

  @Test
  @DisplayName("????????? ??????")
  void increaseHit() {
    boardService.increaseHitByDB(BOARD_2_ID);
    Board board = boardRepository.findById(BOARD_2_ID).get();
    assertThat(board.getHit()).isEqualTo(1);
  }

  @Test
  @DisplayName("????????? ??????")
  void register() {
    BoardRequest boardRequest = BoardRequest.builder()
      .email(MEMBER_1_EMAIL)
      .title("hello")
      .content("hi")
      .boardCategory(BoardCategory.FREE)
      .startDateTime(LocalDateTime.now())
      .endDateTime(LocalDateTime.of(2099, 1, 1, 1, 1, 1))
      .build();

    boardService.write(boardRequest, null);
    BoardSearchCondition condition = BoardSearchCondition.builder()
      .email(MEMBER_1_EMAIL)
      .build();
    Page<BoardQueryResponse> boardQueryResponses = boardService.searchBy(condition, Pageable.ofSize(10));
    assertThat(boardQueryResponses.getContent().get(0).getId()).isNotNull();
  }

  @Test
  @DisplayName("????????? ??????")
  void edit() {
    BoardModifyRequest modifyRequest = BoardModifyRequest.builder()
      .title("hello")
      .boardCategory(BoardCategory.FREE)
      .build();

    BoardResponse boardResponse = boardService.edit(BOARD_1_ID, modifyRequest);

    assertThat(boardResponse.getTitle()).isEqualTo("hello");
    assertThat(boardResponse.getBoardCategory()).isEqualTo(BoardCategory.FREE);
  }

  @Test
  @DisplayName("????????? soft ??????")
  void deleteSoftly() {
    boardService.delete(BOARD_2_ID);
    Board board = boardRepository.findById(BOARD_2_ID).get();

    assertThat(board.getStatus()).isEqualTo(Status.INACTIVE);
    assertThat(board.getDeletedDateTime()).isNotNull();
  }

  @Test
  @DisplayName("????????? ????????? ??????")
  void restore() {
    boardService.delete(BOARD_3_ID);
    boardService.restore(BOARD_3_ID);

    Board board = boardRepository.findById(BOARD_3_ID).get();

    assertThat(board.getStatus()).isEqualTo(Status.ACTIVE);
    assertThat(board.getDeletedDateTime()).isNull();
  }
}