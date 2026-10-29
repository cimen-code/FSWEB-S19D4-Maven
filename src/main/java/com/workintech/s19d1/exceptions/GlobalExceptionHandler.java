package com.workintech.s19d1.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionResponse> handleApiException(
            ApiException exception) {

        log.warn("API error: {}", exception.getMessage());

        HttpStatus status = exception.getHttpStatus();

        return ResponseEntity.status(status).body(
            new ExceptionResponse(
                exception.getMessage(),
                status.value(),
                LocalDateTime.now()
            )
        );
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class
    })
    public ResponseEntity<ExceptionResponse> handleBadRequest(
            Exception exception) {

        log.warn("Bad request: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ExceptionResponse(
                "Invalid request data",
                400,
                LocalDateTime.now()
            )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(
            Exception exception) {

        log.error("Unexpected error", exception);

        return ResponseEntity.status(
            HttpStatus.INTERNAL_SERVER_ERROR
        ).body(
            new ExceptionResponse(
                exception.getMessage(),
                500,
                LocalDateTime.now()
            )
        );
    }
}
