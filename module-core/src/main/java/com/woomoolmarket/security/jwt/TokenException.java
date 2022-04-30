package com.woomoolmarket.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class TokenException extends AuthenticationException {

  public TokenException(String message) {
    super(message);
  }

  public TokenException(String msg, Throwable cause) {
    super(msg, cause);
  }
}