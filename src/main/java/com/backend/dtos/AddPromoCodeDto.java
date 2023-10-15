package com.backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddPromoCodeDto {

    @NotNull(message = "Promocode name is  required")
    private String name;

    @NotNull(message = "Please provide discount amount for the promocode")
    private Double discount_amount;

    @NotNull(message = "Please provide event name")
    private String event_name;

    private String username;
}
