package com.woomoolmarket.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.ModuleServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(classes = ModuleServiceApplication.class)
class RedisConfigTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    Environment env;

    @Test
    void envTest() {
        int port = Integer.parseInt(env.getProperty("spring.redis.port"));
        String host = env.getProperty("spring.redis.host");

        System.out.println("host = " + host);
        System.out.println("port = " + port);
    }

}