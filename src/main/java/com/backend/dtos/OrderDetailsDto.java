package com.backend.dtos;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class OrderDetailsDto {
    private int orderId;
    private String purchasedBy;
    private int quantity;
    private double price;
    private LocalDate purchasedDate;
}
