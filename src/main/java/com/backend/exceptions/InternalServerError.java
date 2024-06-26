package com.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;

public class InternalServerError extends RuntimeException{

    public InternalServerError(String message){
        super(message);
    }
    public InternalServerError(){
        super("Something went wrong, please try again");
    }
}
