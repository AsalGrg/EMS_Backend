package com.backend.exceptions;

public class PaymentException extends RuntimeException{

    public PaymentException(String cause){
        super(cause);
    }
}
