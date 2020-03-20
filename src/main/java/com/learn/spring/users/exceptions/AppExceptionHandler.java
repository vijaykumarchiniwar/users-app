package com.learn.spring.users.exceptions;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {UserException.class})
    private ResponseEntity<Object> exceptionHandler(UserException userException, HttpRequest httpRequest) {
        return new ResponseEntity<>(new ErrorMessage(new Date(), userException.getMessage()), httpRequest.getHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    private ResponseEntity<Object> exceptionHandler(Exception exception, HttpRequest httpRequest) {
        return new ResponseEntity<>(new ErrorMessage(new Date(), exception.getMessage()), httpRequest.getHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
