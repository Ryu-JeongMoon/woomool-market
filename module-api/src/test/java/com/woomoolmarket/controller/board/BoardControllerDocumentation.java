package com.woomoolmarket.controller.board;

import com.woomoolmarket.config.ApiDocumentationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardControllerDocumentation extends ApiDocumentationConfig {

    @BeforeEach
    void initialize() {
        em.createNativeQuery("ALTER TABLE BOARD ALTER COLUMN `board_id` RESTART WITH 1").executeUpdate();
    }

    @Test
    void configTest() {
        System.out.println("yaho");
    }
}
