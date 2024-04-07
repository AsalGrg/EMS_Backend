package com.backend.dtos.payment;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter

public class PaymentRequestDto {

    @NotNull(message = "Event Id is required")
    private Integer event_id;

    @NotNull(message = "Provide the total amount to pay")
    private Double total_amount;

    @Value("1")
    private int quantity;//ticket quantity is 1 by default

    private String promoCode;
}
