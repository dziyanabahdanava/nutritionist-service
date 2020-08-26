package com.epam.ms.controller.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorData {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    @JsonFormat
    private String message;

    private ErrorData() {
        timestamp = LocalDateTime.now();
    }

    public ErrorData(HttpStatus status) {
        this();
    }

    public ErrorData(Throwable ex) {
        this();
        this.message = "Unexpected error";
    }

    public ErrorData(String message, Throwable ex) {
        this();
        this.message = message;
    }
}
