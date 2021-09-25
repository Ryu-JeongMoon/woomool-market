package com.woomoolmarket.errors;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {

    private final String exception;
    private final String message;

    public static Optional<ExceptionResponse> of(String exception, String message) {
        return Optional.of(new ExceptionResponse(exception, message));
    }
}
