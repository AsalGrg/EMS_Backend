package com.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ResourceNotFoundException extends RuntimeException{

    public String message;
    public ResourceNotFoundException(String message){
        super(message);
        this.message= message;
    }
}
