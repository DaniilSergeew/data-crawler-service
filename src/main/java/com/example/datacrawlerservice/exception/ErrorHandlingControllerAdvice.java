package com.example.datacrawlerservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleError(Exception ex) {
        log.error("RuntimeException exception: " + ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
