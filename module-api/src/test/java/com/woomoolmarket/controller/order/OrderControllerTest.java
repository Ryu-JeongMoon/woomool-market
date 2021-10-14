package com.woomoolmarket.controller.order;

import com.woomoolmarket.config.ApiDocumentationConfig;
import org.junit.jupiter.api.BeforeEach;

class OrderControllerTest extends ApiDocumentationConfig {

    @BeforeEach
    void initialize() throws Exception {
        em.createNativeQuery("ALTER TABLE ORDERS ALTER COLUMN `order_id` RESTART WITH 1").executeUpdate();
    }
}