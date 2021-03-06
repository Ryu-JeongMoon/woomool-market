package com.woomoolmarket.controller.board;

import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_EMAIL;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woomoolmarket.config.ApiDocumentationConfig;
import com.woomoolmarket.domain.entity.Board;
import com.woomoolmarket.domain.entity.enumeration.BoardCategory;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.service.board.dto.request.BoardModifyRequest;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import java.time.LocalDateTime;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "panda@naver.com", roles = "USER")
public class BoardControllerDocumentationTest extends ApiDocumentationConfig {

  @BeforeEach
  void init() {
    Member member = memberTestHelper.createMember();
    MEMBER_ID = member.getId();

    Board board = boardTestHelper.createBoard(member);
    BOARD_ID = board.getId();

    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @DisplayName("????????? ?????? ??????")
  void getActiveBoardById() throws Exception {
    mockMvc.perform(
        get("/api/boards/" + BOARD_ID)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL))
      .andDo(document("board/get-board",
        responseFields(
          fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????? ??????"),
          fieldWithPath("title").type(JsonFieldType.STRING).description("??????"),
          fieldWithPath("content").type(JsonFieldType.STRING).description("??????"),
          fieldWithPath("hit").type(JsonFieldType.NUMBER).description("?????????"),
          fieldWithPath("boardCategory").type(JsonFieldType.STRING).description("????????? ??????"),
          fieldWithPath("startDateTime").type(JsonFieldType.VARIES).description("?????? ????????????"),
          fieldWithPath("endDateTime").type(JsonFieldType.VARIES).description("?????? ????????????"),
          fieldWithPath("createdDateTime").type(JsonFieldType.VARIES).description("????????????"),
          subsectionWithPath("boardCount").type(JsonFieldType.OBJECT).description("????????? & ????????????"),
          subsectionWithPath("memberResponse").type(JsonFieldType.OBJECT).description("????????? ??????"),
          subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS")
        )));
  }

  @Test
  @DisplayName("????????? ?????? ??????")
  void getListBySearchConditionForMember() throws Exception {
    mockMvc.perform(
        get("/api/boards")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL))
      .andDo(document("board/get-boards",
        relaxedResponseFields(
          subsectionWithPath("_embedded").type(JsonFieldType.OBJECT).description("????????? ?????????"),
          subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS"),
          subsectionWithPath("page").type(JsonFieldType.OBJECT).description("????????? ??????")
        )));
  }

  @Test
  @DisplayName("????????? ??????")
  void registerBoard() throws Exception {
    BoardRequest boardRequest = BoardRequest.builder()
      .email(MEMBER_EMAIL)
      .title("polar")
      .content("bear")
      .startDateTime(LocalDateTime.now())
      .endDateTime(LocalDateTime.of(2099, 1, 1, 1, 1, 1))
      .boardCategory(BoardCategory.FREE)
      .build();

    mockMvc.perform(
        post("/api/boards")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL)
          .content(objectMapper.writeValueAsString(boardRequest)))
      .andDo(document("board/create-board",
        requestFields(
          fieldWithPath("email").type(JsonFieldType.STRING).description("?????????")
            .attributes(key(CONSTRAINT).value("????????? ?????? 9-64???")),
          fieldWithPath("title").type(JsonFieldType.STRING).description("??????")
            .attributes(key(CONSTRAINT).value("?????? 255???")),
          fieldWithPath("content").type(JsonFieldType.STRING).description("??????")
            .attributes(key(CONSTRAINT).value("?????? 65535???")),
          fieldWithPath("boardCategory").type(JsonFieldType.STRING).description("????????? ??????")
            .attributes(key(CONSTRAINT).value("FREE, QNA, NOTICE ??? ??????")),
          fieldWithPath("startDateTime").type(JsonFieldType.VARIES).description("?????? ????????????")
            .attributes(key(CONSTRAINT).value("?????????????????? ?????? ??? ??????")),
          fieldWithPath("endDateTime").type(JsonFieldType.VARIES).description("?????? ????????????")
            .attributes(key(CONSTRAINT).value("?????????????????? ?????? ??? ??????"))
        )));
  }

  @Test
  @DisplayName("????????? ??????")
  void editBoardInfo() throws Exception {
    BoardModifyRequest boardModifyRequest = BoardModifyRequest.builder()
      .title("bear")
      .content("panda")
      .build();

    mockMvc.perform(
        patch("/api/boards")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL)
          .content(objectMapper.writeValueAsString(boardModifyRequest)))
      .andDo(document("board/edit-board",
        requestFields(
          fieldWithPath("title").type(JsonFieldType.STRING).description("??????").optional()
            .attributes(key(CONSTRAINT).value("?????? 255???")),
          fieldWithPath("content").type(JsonFieldType.STRING).description("??????").optional()
            .attributes(key(CONSTRAINT).value("?????? 65535???")),
          fieldWithPath("boardCategory").type(JsonFieldType.STRING).description("????????? ??????").optional()
            .attributes(key(CONSTRAINT).value("FREE, QNA, NOTICE ??? ??????"))
        )));
  }

  @Test
  @DisplayName("????????? ??????")
  void deleteBoard() throws Exception {
    mockMvc.perform(
        delete("/api/boards/" + BOARD_ID)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL))
      .andExpect(status().isNoContent())
      .andDo(document("board/delete-board"));
  }

  @Test
  @DisplayName("????????? ??????")
  @WithMockUser(roles = "ADMIN")
  void restoreBoard() throws Exception {
    boardService.delete(BOARD_ID);

    mockMvc.perform(
        get("/api/boards/deleted/" + BOARD_ID)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL))
      .andExpect(status().isCreated())
      .andDo(document("board/restore-board"));
  }

  @Test
  @DisplayName("????????? ?????? ?????? ??????")
  @WithMockUser(roles = "ADMIN")
  void getListBySearchConditionForAdmin() throws Exception {
    mockMvc.perform(
        get("/api/boards/admin")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL))
      .andDo(document("board/admin-get-boards",
        relaxedResponseFields(
          subsectionWithPath("_embedded.boardQueryResponseList").type(JsonFieldType.ARRAY).description("????????? ?????????"),
          subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS"),
          subsectionWithPath("page").type(JsonFieldType.OBJECT).description("????????? ??????")
        )));
  }
}