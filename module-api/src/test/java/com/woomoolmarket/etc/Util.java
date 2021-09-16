package com.woomoolmarket.etc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
class Util {

    @Test
    @DisplayName("log4j2 주입이 올바르게 된다")
    void log4j2Test() {
        log.info("log -> {}", log.getClass());
    }

    // now.toString() = "2021-09-15T23:46:41.364500" 일 때, result는 끝의 자리 0 생략됨 추후 수정 필요
    @Test
    @DisplayName("LocalDateTime 형식으로 올바르게 작성된다")
    void objectMapperTest() throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // 추가
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String result = mapper.writeValueAsString(now);
        log.info("result = {}", result);

        Assertions.assertThat(now.toString()).isEqualTo(result.replace("\"", ""));
    }

    @Test
    @DisplayName("LocalDateTime 형식으로 읽어온다")
    void objectMapperReadLocalDateTimeTest() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        LocalDateTime now = LocalDateTime.now();
        String nowStr = objectMapper.writeValueAsString(now);
        log.info(nowStr);

        Object obj = objectMapper.readValue(nowStr, Object.class);
        log.info(obj);
    }
}
