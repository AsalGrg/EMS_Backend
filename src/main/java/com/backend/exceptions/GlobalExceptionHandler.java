package com.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody ErrorFormat handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return new ErrorFormat(ex.getMessage(), HttpStatus.CONFLICT, LocalDate.now());
    }

    @ExceptionHandler(InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorFormat handleInternalServerError(InternalServerError ex) {
        return new ErrorFormat(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDate.now());
    }


    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody ErrorFormat handleNotAuthorizedException(NotAuthorizedException ex) {
        return new ErrorFormat(ex.getMessage(), HttpStatus.UNAUTHORIZED, LocalDate.now());
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorFormat handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ErrorFormat(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDate.now());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorFormat handleIllegalStateException(IllegalStateException ex){
        return new ErrorFormat(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDate.now());
    }

}
