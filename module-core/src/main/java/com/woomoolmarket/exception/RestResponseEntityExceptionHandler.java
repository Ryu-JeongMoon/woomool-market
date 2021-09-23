package com.woomoolmarket.exception;


import com.woomoolmarket.errors.ErrorResponse;
import com.woomoolmarket.exception.MemberDuplicatedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {MemberDuplicatedException.class})
    protected ErrorResponse badRequest(RuntimeException ex, WebRequest request) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

}