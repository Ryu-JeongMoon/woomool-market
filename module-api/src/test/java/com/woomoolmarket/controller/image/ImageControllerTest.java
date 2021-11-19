package com.woomoolmarket.controller.image;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woomoolmarket.config.ApiControllerConfig;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.image.entity.Image;
import com.woomoolmarket.domain.member.entity.Member;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ImageControllerTest extends ApiControllerConfig {

    @BeforeEach
    void init() {
        Image image = imageTestHelper.createImage();
        IMAGE_ID = image.getId();

        Member member = memberTestHelper.createUser();
        Board board = boardTestHelper.createBoard(member);
        BOARD_ID = board.getId();

        board.addImages(List.of(image));
    }

    @Test
    @DisplayName("게시글 번호로 이미지 조회 성공")
    void getImages() throws Exception {
        mockMvc.perform(
                get("/api/images/" + BOARD_ID))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded").exists());
    }

    @Test
    @DisplayName("이미지 번호로 삭제 성공")
    void deleteByImage() throws Exception {
        mockMvc.perform(
                delete("/api/images/" + IMAGE_ID))
            .andDo(print())
            .andExpect(status().isNoContent());
    }
}