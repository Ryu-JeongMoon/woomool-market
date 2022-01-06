package com.woomoolmarket.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {

  private final String exception;
  private final String message;

  public static ExceptionResponse of(String exception, String message) {
    return new ExceptionResponse(exception, message);
  }
}
