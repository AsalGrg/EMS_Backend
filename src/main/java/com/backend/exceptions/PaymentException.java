package com.backend.exceptions;

public class PaymentException extends Exception{

    public PaymentException(Throwable cause){
        super("Error processing payment", cause);
    }
}
