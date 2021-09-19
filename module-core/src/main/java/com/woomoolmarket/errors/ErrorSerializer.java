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
public class ErrorSerializer extends JsonSerializer<BindingResult> {

    @Override
    public void serialize(BindingResult bindingResult, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
        gen.writeStartArray();
        bindingResult.getFieldErrors().forEach(e -> {
            try {
                gen.writeStartObject();
                gen.writeStringField("field", e.getField());
                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("code", e.getCode());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());
                Object rejectedValue = e.getRejectedValue();

                if (rejectedValue != null) {
                    gen.writeStringField("rejectedValue", rejectedValue.toString());
                }

                gen.writeEndObject();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });

        bindingResult.getGlobalErrors().forEach(e -> {
            try {
                gen.writeStartObject();
                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("code", e.getCode());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());
                gen.writeEndObject();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });
        gen.writeEndArray();
    }
}


