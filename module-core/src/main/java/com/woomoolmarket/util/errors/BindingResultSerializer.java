package com.woomoolmarket.util.errors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.BindingResult;

@Slf4j
@JsonComponent
public class BindingResultSerializer extends JsonSerializer<BindingResult> {

  @Override
  public void serialize(BindingResult bindingResult, JsonGenerator jsonGenerator, SerializerProvider sp) throws IOException {
    jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
    jsonGenerator.writeStartArray();
    bindingResult.getFieldErrors().forEach(e -> {
      try {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("exception", e.getClass().getSimpleName());
        jsonGenerator.writeStringField("field", e.getField());
        jsonGenerator.writeStringField("objectName", e.getObjectName());
        jsonGenerator.writeStringField("code", e.getCode());
        jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
        Object rejectedValue = e.getRejectedValue();
        if (rejectedValue != null) {
          jsonGenerator.writeStringField("rejectedValue", rejectedValue.toString());
        }
        jsonGenerator.writeEndObject();
      } catch (IOException ioe) {
        log.info("[WOOMOOL-ERROR] :: BindingResult Error => {}", ioe.getMessage());
      }
    });

    bindingResult.getGlobalErrors().forEach(e -> {
      try {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("objectName", e.getObjectName());
        jsonGenerator.writeStringField("code", e.getCode());
        jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
        jsonGenerator.writeEndObject();
      } catch (IOException ioe) {
        log.info("[WOOMOOL-ERROR] :: BindingResult Error => {}", ioe.getMessage());
      }
    });
    jsonGenerator.writeEndArray();
  }
}


