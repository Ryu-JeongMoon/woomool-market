package com.woomoolmarket.errors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.BindingResult;

@Log4j2
@JsonComponent
public class BindingResultSerializer extends JsonSerializer<BindingResult> {

    @Override
    public void serialize(BindingResult bindingResult, JsonGenerator jsonGenerator, SerializerProvider sp) throws IOException {
        jsonGenerator.writeStartArray();
        bindingResult.getFieldErrors().forEach(e -> {
            try {
                jsonGenerator.writeStartObject();
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
                ioe.printStackTrace();
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
                ioe.printStackTrace();
            }
        });
        jsonGenerator.writeEndArray();
    }
}


