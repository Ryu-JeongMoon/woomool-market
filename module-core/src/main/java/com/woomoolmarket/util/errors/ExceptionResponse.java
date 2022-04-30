package com.woomoolmarket.util.errors;

public record ExceptionResponse(String exception, String message) {

  public static ExceptionResponse of(String exception, String message) {
    return new ExceptionResponse(exception, message);
  }
}
