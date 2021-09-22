package com.woomoolmarket.etc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
class Util {

    @Test
    @DisplayName("log4j2 주입이 올바르게 된다")
    void log4j2Test() {
        log.info("log -> {}", log.getClass());
    }

    ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("LocalDateTime 형식으로 올바르게 작성된다")
    void objectMapperTest() throws JsonProcessingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        String result = objectMapper.writeValueAsString(now);
        log.info("result = {}", result);
        assertEquals(now, result.replace("\"", ""));
    }

    @Test
    @DisplayName("LocalDateTime 형식으로 읽어온다")
    void objectMapperReadLocalDateTimeTest() throws JsonProcessingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        String nowStr = objectMapper.writeValueAsString(now);
        Object obj = objectMapper.readValue(nowStr, Object.class);
        log.info(obj);
        assertEquals(nowStr.replace("\"", ""), obj);
    }
}
