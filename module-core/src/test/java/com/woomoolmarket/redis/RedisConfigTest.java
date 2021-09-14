package com.woomoolmarket.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.ModuleCoreApplication;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@SpringBootTest(classes = ModuleCoreApplication.class)
class RedisConfigTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    Environment env;

    @Test
    void envTest() {
        int port = Integer.parseInt(env.getProperty("spring.redis.port"));
        String host = env.getProperty("spring.redis.host");
        log.info("host = {}", host);
        log.info("port = {}", port);
    }

}