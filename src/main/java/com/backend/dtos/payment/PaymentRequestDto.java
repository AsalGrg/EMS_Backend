package com.backend.dtos.payment;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter

public class PaymentRequestDto {

    private String username;

    @NotNull(message = "Provide the total amount to pay")
    private Double total_amount;

    @Value("1")
    private int quantity;//ticket quantity is 1 by default


    @NotNull(message = "Event name should be provided")
    private String event_name;

    private String promoCode;
}
