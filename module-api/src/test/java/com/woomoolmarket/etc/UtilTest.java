package com.woomoolmarket.etc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

@Slf4j
class UtilTest {

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
    log.info("obj = {}", obj);
    assertEquals(nowStr.replace("\"", ""), obj);
  }

  @Test
  @DisplayName("toList vs toUnmodifiableList 시간 비교")
  void unmodifiableTest() {
    List<Integer> integers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    StopWatch stopWatch1 = new StopWatch();
    stopWatch1.start();
    integers.stream().map(i -> i + 1).collect(Collectors.toList());
    stopWatch1.stop();
    log.info(stopWatch1.prettyPrint());

    StopWatch stopWatch2 = new StopWatch();
    stopWatch2.start();
    integers.stream().map(i -> i + 1).toList();
    stopWatch2.stop();
    log.info(stopWatch2.prettyPrint());

  }
}
