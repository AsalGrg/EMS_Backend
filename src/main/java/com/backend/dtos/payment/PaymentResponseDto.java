package com.backend.dtos.payment;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PaymentResponseDto {

    private String username;

    private String event_name;

    private int quantity;

    private double unit_price;

    private double net_total;

    private String promoCode;

    private double grand_total;

    private double amount_saved;
}
