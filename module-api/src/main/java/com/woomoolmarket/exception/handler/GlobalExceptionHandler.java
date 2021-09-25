package com.woomoolmarket.exception.handler;

import com.woomoolmarket.aop.exception.LogForException;
import com.woomoolmarket.errors.ExceptionResponse;
import com.woomoolmarket.exception.member.UsernameDuplicatedException;
import com.woomoolmarket.exception.product.NotEnoughStockException;
import com.woomoolmarket.exception.product.ProductNameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity handleNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = UsernameDuplicatedException.class)
    public ResponseEntity handleUsernameDuplicatedException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = ProductNameNotFoundException.class)
    public ResponseEntity handleProductNameNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }

    @ExceptionHandler(value = NotEnoughStockException.class)
    public ResponseEntity handleNotEnoughStockException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
    }
}
