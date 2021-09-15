package com.woomoolmarket.redis.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.entity.Member;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
class CacheConfigTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void cacheTest() throws JsonProcessingException {
        Member panda = Member.builder()
            .userId("panda")
            .password("1234")
            .leaveDate(LocalDateTime.now())
            .address(new Address("seoul", "jeonju", "busan"))
            .build();

        String str = objectMapper.writeValueAsString(panda);
        Object obj = objectMapper.readValue(str, Object.class);
        log.info("str = {}", str);
        log.info("obj = {}", obj);
    }
}