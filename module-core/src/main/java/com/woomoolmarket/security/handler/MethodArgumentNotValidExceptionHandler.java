package com.woomoolmarket.security.handler;


import com.woomoolmarket.errors.ErrorResponse;
import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private ErrorResponse processFieldErrors(List<FieldError> fieldErrors) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "@Valid Error");
        for (FieldError fieldError : fieldErrors) {
            errorResponse.addFieldError(fieldError.getObjectName(), fieldError.getField(),
                fieldError.getDefaultMessage());
        }
        return errorResponse;
    }
}