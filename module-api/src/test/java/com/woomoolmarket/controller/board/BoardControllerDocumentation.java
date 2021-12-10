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
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.member.entity.Member;
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
public class BoardControllerDocumentation extends ApiDocumentationConfig {

    @BeforeEach
    void init() {
        Member member = memberTestHelper.createUser();
        MEMBER_ID = member.getId();

        Board board = boardTestHelper.createBoard(member);
        BOARD_ID = board.getId();

        Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void getActiveBoardById() throws Exception {
        mockMvc.perform(
                get("/api/boards/" + BOARD_ID)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL))
            .andDo(document("board/get-board",
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 고유 번호"),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("hit").type(JsonFieldType.NUMBER).description("조회수"),
                    fieldWithPath("boardCategory").type(JsonFieldType.STRING).description("게시글 분류"),
                    fieldWithPath("startDateTime").type(JsonFieldType.VARIES).description("게시 시작일시"),
                    fieldWithPath("endDateTime").type(JsonFieldType.VARIES).description("게시 종료일시"),
                    fieldWithPath("createdDateTime").type(JsonFieldType.VARIES).description("생성일시"),
                    subsectionWithPath("memberResponse").type(JsonFieldType.OBJECT).description("작성자 정보"),
                    subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS")
                )));
    }

    @Test
    @DisplayName("게시글 전체 조회")
    void getListBySearchConditionForMember() throws Exception {
        mockMvc.perform(
                get("/api/boards")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL))
            .andDo(document("board/get-boards",
                relaxedResponseFields(
                    subsectionWithPath("_embedded").type(JsonFieldType.OBJECT).description("게시글 리스트"),
                    subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS"),
                    subsectionWithPath("page").type(JsonFieldType.OBJECT).description("페이지 설정")
                )));
    }

    @Test
    @DisplayName("게시글 작성")
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
                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        .attributes(key("constraint").value("이메일 형식 9-64자")),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목")
                        .attributes(key("constraint").value("최대 255자")),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                        .attributes(key("constraint").value("최대 65535자")),
                    fieldWithPath("boardCategory").type(JsonFieldType.STRING).description("게시글 분류")
                        .attributes(key("constraint").value("FREE, QNA, NOTICE 중 하나")),
                    fieldWithPath("startDateTime").type(JsonFieldType.VARIES).description("게시 시작일시")
                        .attributes(key("constraint").value("종료일시보다 느릴 수 없음")),
                    fieldWithPath("endDateTime").type(JsonFieldType.VARIES).description("게시 종료일시")
                        .attributes(key("constraint").value("현재시각보다 앞설 수 없음"))
                )));
    }

    @Test
    @DisplayName("게시글 수정")
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
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목").optional()
                        .attributes(key("constraint").value("최대 255자")),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용").optional()
                        .attributes(key("constraint").value("최대 65535자")),
                    fieldWithPath("boardCategory").type(JsonFieldType.STRING).description("게시글 분류").optional()
                        .attributes(key("constraint").value("FREE, QNA, NOTICE 중 하나"))
                )));
    }

    @Test
    @DisplayName("게시글 삭제")
    void deleteBoard() throws Exception {
        mockMvc.perform(
                delete("/api/boards/" + BOARD_ID)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL))
            .andExpect(status().isNoContent())
            .andDo(document("board/delete-board"));
    }

    @Test
    @DisplayName("게시글 복구")
    @WithMockUser(roles = "ADMIN")
    void restoreBoard() throws Exception {
        boardService.deleteSoftly(BOARD_ID);

        mockMvc.perform(
                get("/api/boards/deleted/" + BOARD_ID)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL))
            .andExpect(status().isCreated())
            .andDo(document("board/restore-board"));
    }

    @Test
    @DisplayName("관리자 전용 전체 조회")
    @WithMockUser(roles = "ADMIN")
    void getListBySearchConditionForAdmin() throws Exception {
        mockMvc.perform(
                get("/api/boards/admin")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL))
            .andDo(document("board/admin-get-boards",
                relaxedResponseFields(
                    subsectionWithPath("_embedded.boardResponseList").type(JsonFieldType.ARRAY).description("게시글 리스트"),
                    subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS"),
                    subsectionWithPath("page").type(JsonFieldType.OBJECT).description("페이지 설정")
                )));
    }
}