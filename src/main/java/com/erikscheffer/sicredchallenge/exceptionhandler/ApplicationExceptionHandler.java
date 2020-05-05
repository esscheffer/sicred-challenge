package com.erikscheffer.sicredchallenge.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        return handleExceptionInternal(
                ex,
                new ErrorMessageWrapper("Resource not found", ex.toString()),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @Getter
    @AllArgsConstructor
    private static class ErrorMessageWrapper {
        private final String userMessage;
        private final String developerMessage;
    }
}
