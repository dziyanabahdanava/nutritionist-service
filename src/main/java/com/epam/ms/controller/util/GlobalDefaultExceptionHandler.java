package com.epam.ms.controller.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalDefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorData> constraintViolationErrorHandler(ConstraintViolationException e) {
        ErrorData errorData = new ErrorData(e.getMessage(), e);
        return ResponseEntity.badRequest().body(errorData);
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ResponseEntity<Void> constraintViolationErrorHandler(EmptyResultDataAccessException e) {
        log.error("Resource not found", e);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorData> baseErrorHandler(Exception e) {
        ErrorData errorData = new ErrorData(e.getMessage(), e);
        log.error("Unexpected error", e);
        return ResponseEntity.status(500).body(errorData);
    }
}
