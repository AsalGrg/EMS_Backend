package com.backend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ErrorFormat {

    String message;
    HttpStatus status;
    LocalDate timeStamp;

    public ErrorFormat(String message){
        super();
       this.message= message;
    }
}
