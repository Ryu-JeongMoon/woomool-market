package com.woomoolmarket.config;

import static java.util.Locale.KOREA;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatModule extends SimpleModule {

  public DateTimeFormatModule() {
    String datetimeFormat = "yyyy-MM-dd HH:mm:ss";
    addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(datetimeFormat, KOREA)));
    addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(datetimeFormat, KOREA)));
  }
}
