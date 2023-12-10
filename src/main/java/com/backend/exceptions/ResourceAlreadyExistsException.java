package com.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;


public class ResourceAlreadyExistsException extends RuntimeException{

//    String message;

    public ResourceAlreadyExistsException(String message){
        super(message);
//        this.message= message;
    }
}
