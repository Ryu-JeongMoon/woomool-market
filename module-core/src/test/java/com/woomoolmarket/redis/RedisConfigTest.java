package com.woomoolmarket.redis;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.ModuleCoreApplication;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@SpringBootTest
class RedisConfigTest {

    @Autowired
    Environment env;

    @Test
    @DisplayName("application.yml의 속성을 가져온다")
    void envTest() {
        int port = Integer.parseInt(env.getProperty("spring.redis.port"));
        String host = env.getProperty("spring.redis.host");

        assertThat(port).isEqualTo(6379);
        assertThat(host).isEqualTo("localhost");
    }

}