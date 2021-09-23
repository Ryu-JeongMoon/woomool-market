package com.woomoolmarket.exception.handler;

import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@Controller
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static String getExceptionName(Exception e) {
        return e.getClass().getSimpleName();
    }

    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception e, Model model) {
        log.error(e.getStackTrace());

        model.addAllAttributes(
            Map.of("exceptionName", getExceptionName(e), "exceptionMessage", e.getMessage()));

        return "exceptions";
    }
}
