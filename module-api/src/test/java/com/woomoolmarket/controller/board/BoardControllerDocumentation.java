package com.woomoolmarket.controller.board;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.woomoolmarket.config.ApiDocumentationConfig;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

public class BoardControllerDocumentation extends ApiDocumentationConfig {

    private static final String USERNAME = "panda@naver.com";
    private static final String PASSWORD = "123456";
    private static final String NICKNAME = "panda";
    private static Long MEMBER_ID;
    private static Long BOARD_ID;

    @BeforeEach
    void initialize() {
        em.createNativeQuery("ALTER TABLE BOARD ALTER COLUMN `board_id` RESTART WITH 1").executeUpdate();

        Member member = Member.builder()
            .email(USERNAME)
            .password(PASSWORD)
            .nickname(NICKNAME)
            .build();
        MEMBER_ID = memberRepository.save(member).getId();

        Board board = Board.builder()
            .member(member)
            .title("panda")
            .content("bear")
            .boardCategory(BoardCategory.NOTICE)
            .build();
        BOARD_ID = boardRepository.save(board).getId();
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
                    subsectionWithPath("_embedded.boardResponseList").type(JsonFieldType.ARRAY).description("게시글 리스트"),
                    subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS"),
                    subsectionWithPath("page").type(JsonFieldType.OBJECT).description("페이지 설정")
                )));
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
                    subsectionWithPath("member").type(JsonFieldType.OBJECT).description("작성자"),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("hit").type(JsonFieldType.NUMBER).description("조회수"),
                    fieldWithPath("boardCategory").type(JsonFieldType.STRING).description("게시글 분류")
                )));
    }

    @Test
    @DisplayName("게시글 작성")
    @WithMockUser(username = USERNAME, roles = "USER")
    void registerBoard() throws Exception {
        BoardRequest boardRequest = BoardRequest.builder()
            .email(USERNAME)
            .title("polar")
            .content("bear")
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
                        .attributes(key("constraint").value("FREE, QNA, NOTICE 중 하나"))
                )));
    }

    @Test
    @DisplayName("게시글 수정")
    void editBoardInfo() {

    }

    @Test
    @DisplayName("게시글 삭제")
    void deleteBoard() {

    }

    @Test
    @DisplayName("게시글 복구")
    void restoreBoard() {

    }

    @Test
    @DisplayName("관리자 전용 전체 조회")
    void getListBySearchConditionForAdmin() {

    }
}
