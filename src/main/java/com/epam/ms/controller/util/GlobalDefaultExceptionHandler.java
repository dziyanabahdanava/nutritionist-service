package com.epam.ms.controller.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalDefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorData> constraintViolationErrorHandler(ConstraintViolationException e) {
        ErrorData errorData = new ErrorData(e.getMessage(), e);
        return ResponseEntity.badRequest().body(errorData);
    }
}
