package com.woomoolmarket.security.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class ErrorResponse {

    private final int status;
    private final String message;
    private final List<FieldError> fieldErrors = new ArrayList<>();

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public void addFieldError(String objectName, String path, String message) {
        FieldError error = new FieldError(objectName, path, message);
        fieldErrors.add(error);
    }
}
