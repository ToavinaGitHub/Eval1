package com.eval1.mg.Security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler {

    @ExceptionHandler(value = { NoResourceFoundException.class })
    public ResponseEntity<Object> noResourceFoundException(Exception ex) {
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/error/404").body(null);
    }
}
