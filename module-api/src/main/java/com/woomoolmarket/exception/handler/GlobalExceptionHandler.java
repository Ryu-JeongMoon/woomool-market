package com.woomoolmarket.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.woomoolmarket.aop.exception.LogForException;
import com.woomoolmarket.errors.ExceptionResponse;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ResponseBody
@LogForException
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static String getExceptionClass(Exception e) {
        return e != null ? e.getClass().getSimpleName() : "";
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Optional<ExceptionResponse>> illegalArgumentExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<Optional<ExceptionResponse>> illegalStateExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<Optional<ExceptionResponse>> authenticationExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Optional<ExceptionResponse>> entityNotFoundExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Optional<ExceptionResponse>> usernameNotFoundExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = JsonProcessingException.class)
    public ResponseEntity<Optional<ExceptionResponse>> jsonProcessingExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Optional<ExceptionResponse>> methodArgumentNotValidExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Optional<ExceptionResponse>> accessDeniedExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }
}
