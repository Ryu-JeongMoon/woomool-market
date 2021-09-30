package com.woomoolmarket.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.woomoolmarket.aop.exception.LogForException;
import com.woomoolmarket.errors.ExceptionResponse;
import javax.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity handleIllegalArgumentException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity handleUsernameNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = JsonProcessingException.class)
    public ResponseEntity handleJsonProcessingException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }
}
