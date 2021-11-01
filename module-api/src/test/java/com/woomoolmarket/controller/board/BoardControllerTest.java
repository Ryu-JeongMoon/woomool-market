package com.woomoolmarket.controller.board;

import static com.woomoolmarket.helper.BoardTestHelper.BOARD_CONTENT;
import static com.woomoolmarket.helper.BoardTestHelper.BOARD_TITLE;
import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_EMAIL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woomoolmarket.config.ApiControllerConfig;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.service.board.dto.request.BoardModifyRequest;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "panda@naver.com", roles = "USER")
class BoardControllerTest extends ApiControllerConfig {

    @BeforeEach
    void init() {
        Member member = memberTestHelper.createUser();
        MEMBER_ID = member.getId();

        Board board = boardTestHelper.createBoard(member);
        BOARD_ID = board.getId();

        stringRedisTemplate.keys("*").forEach(k -> stringRedisTemplate.delete(k));
    }

    @Test
    @DisplayName("게시글 목록 조회 성공")
    void getListBySearchCondition() throws Exception {
        mockMvc.perform(
                get("/api/boards")
                    .accept(MediaType.ALL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.boardResponseList[0].id").value(BOARD_ID))
            .andExpect(jsonPath("_embedded.boardResponseList[0].title").value(BOARD_TITLE))
            .andExpect(jsonPath("_embedded.boardResponseList[0].content").value(BOARD_CONTENT))
            .andExpect(jsonPath("_embedded.boardResponseList[0].hit").value(0))
            .andExpect(jsonPath("_embedded.boardResponseList[0].boardCategory").value("NOTICE"))
            .andExpect(jsonPath("_links").exists())
            .andExpect(jsonPath("page").exists());
    }

    @Test
    @DisplayName("게시글 목록 조회 실패 - 405 method not allowed")
    void getListBySearchConditionFail() throws Exception {
        mockMvc.perform(
                patch("/api/boards"))
            .andDo(print())
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("게시글 단건 조회 성공")
    void getActiveBoardById() throws Exception {
        mockMvc.perform(
                get("/api/boards/" + BOARD_ID))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(BOARD_ID))
            .andExpect(jsonPath("memberResponse").exists())
            .andExpect(jsonPath("title").value(BOARD_TITLE))
            .andExpect(jsonPath("content").value(BOARD_CONTENT))
            .andExpect(jsonPath("hit").value(1))
            .andExpect(jsonPath("_links").exists());
    }

    @Test
    @DisplayName("게시글 단건 조회 실패 - 404 존재하지 않는 게시글")
    void getActiveBoardByIdFail() throws Exception {
        mockMvc.perform(
                get("/api/boards/" + 0))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("게시글 추가 성공")
    void registerBoard() throws Exception {
        BoardRequest boardRequest = BoardRequest.builder()
            .email(MEMBER_EMAIL)
            .title("white")
            .content("black")
            .boardCategory(BoardCategory.FREE)
            .build();

        mockMvc.perform(
                post("/api/boards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(boardRequest)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("게시글 추가 실패 - 400 @Valid 작동")
    void registerBoardFail() throws Exception {
        BoardRequest boardRequest = BoardRequest.builder()
            .email(MEMBER_EMAIL)
            .title("white")
            .boardCategory(BoardCategory.FREE)
            .build();

        mockMvc.perform(
                post("/api/boards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(boardRequest)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void editBoardInfo() throws Exception {
        BoardModifyRequest modifyRequest = BoardModifyRequest.builder()
            .title("PANDA")
            .content("BEAR")
            .build();

        mockMvc.perform(
                patch("/api/boards/" + BOARD_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(modifyRequest)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 400 @Valid 작동")
    void editBoardInfoFail() throws Exception {
        BoardModifyRequest modifyRequest = BoardModifyRequest.builder()
            .title("1")
            .build();

        mockMvc.perform(
                patch("/api/boards/" + BOARD_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(modifyRequest)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void deleteBoard() throws Exception {
        mockMvc.perform(
                delete("/api/boards/" + BOARD_ID))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 403 권한 없음")
    @WithMockUser(username = "object", roles = "USER")
    void deleteBoardFail() throws Exception {
        mockMvc.perform(
                delete("/api/boards/" + BOARD_ID))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("삭제된 게시글 복구 성공")
    @WithMockUser(roles = "ADMIN")
    void restoreBoard() throws Exception {
        mockMvc.perform(
                delete("/api/boards/" + BOARD_ID))
            .andDo(print())
            .andExpect(status().isNoContent());

        mockMvc.perform(
                get("/api/boards/deleted/" + BOARD_ID))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("삭제된 게시글 복구 실패 - 403 권한 없음")
    void restoreBoardFail() throws Exception {
        mockMvc.perform(
                delete("/api/boards/" + BOARD_ID))
            .andDo(print())
            .andExpect(status().isNoContent());

        mockMvc.perform(
                get("/api/boards/deleted/" + BOARD_ID))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("관리자 게시글 목록 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void getListBySearchConditionForAdmin() throws Exception {
        mockMvc.perform(
                get("/api/boards/admin"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.boardResponseList[0].id").value(BOARD_ID))
            .andExpect(jsonPath("_embedded.boardResponseList[0].title").value(BOARD_TITLE))
            .andExpect(jsonPath("_embedded.boardResponseList[0].content").value(BOARD_CONTENT))
            .andExpect(jsonPath("_embedded.boardResponseList[0].hit").value(0));
    }

    @Test
    @DisplayName("관리자 검색어로 게시글 목록 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void getListBySearchConditionForAdminFail() throws Exception {
        mockMvc.perform(
                get("/api/boards/admin?boardCategory=" + BoardCategory.FREE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.boardResponseList[0].id").doesNotExist());
    }
}