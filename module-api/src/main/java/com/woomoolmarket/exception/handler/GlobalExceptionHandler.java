package com.woomoolmarket.exception.handler;

import com.woomoolmarket.errors.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestController
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static String getExceptionName(Exception e) {
        return e != null ? e.getClass().getSimpleName() : "";
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, Model model) {
        log.error(e.getStackTrace());
        return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
    }
}
