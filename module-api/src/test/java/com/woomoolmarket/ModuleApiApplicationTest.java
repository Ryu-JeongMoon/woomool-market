package com.woomoolmarket;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.woomoolmarket.config.format.DateTimeFormatModule;
import com.woomoolmarket.util.errors.JsonBindingResultModule;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ModuleApiApplicationTest {

  private final ObjectMapper objectMapper = new ObjectMapper()
    .findAndRegisterModules()
    .enable(SerializationFeature.INDENT_OUTPUT)
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .registerModules(new JavaTimeModule(), new JsonBindingResultModule(), new DateTimeFormatModule());

  @Test
  @DisplayName("LocalDateTime 자체에서는 구분자로 T 사용")
  void serializeTest() throws JsonProcessingException {
    String datetimeFormat = "yyyy-MM-dd HH:mm:ss";
    String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern(datetimeFormat));
    LocalDateTime nowTime = LocalDateTime.parse(now, DateTimeFormatter.ofPattern(datetimeFormat));
    Person panda = new Person("panda", nowTime);
    String pandaJson = objectMapper.writeValueAsString(panda);

    String result = "{\n"
      + "  \"name\" : \"panda\",\n"
      + "  \"birth\" : \"" + now + "\"\n"
      + "}";
    assertThat(pandaJson).isEqualTo(result);
  }

  @Test
  @DisplayName("")
  void deserializeTest() throws IOException {
    String json = "{\n  \"name\" : \"panda\",\n  \"birth\" : \"1000-01-01 01:01:01\"\n}";
    Person panda = new Person("panda", LocalDateTime.of(1000, 1, 1, 1, 1, 1));
    String result = objectMapper.writeValueAsString(panda);
    assertThat(result).isEqualTo(json);
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  static class Person implements Serializable {

    private String name;
    private LocalDateTime birth;
  }
}