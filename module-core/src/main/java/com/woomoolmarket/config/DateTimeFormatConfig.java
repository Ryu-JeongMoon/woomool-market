package com.woomoolmarket.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateTimeFormatConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    String dateFormat = "yyyy-MM-dd";
    String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

    return jacksonObjectMapperBuilder -> {
      jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("Asia/Seoul"));
      jacksonObjectMapperBuilder.simpleDateFormat(datetimeFormat);
      jacksonObjectMapperBuilder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
      jacksonObjectMapperBuilder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(datetimeFormat)));
    };
  }
}
