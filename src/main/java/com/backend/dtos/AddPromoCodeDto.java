package com.backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AddPromoCodeDto {

    @NotNull(message = "Event id is required")
    private Integer eventId;

    @NotNull(message = "Promocode name is  required")
    private String name;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    private boolean hasNoEnd;

    @NotNull(message = "Promo code type is required")
    private String promoCodeType;

    @NotNull(message = "Please provide discount amount for the promocode")
    private Double discount;

    @NotNull(message = "Applicable amount for promo code is required")
    private String applicableOn;

    @NotNull(message = "Promo code limit is required")
    private String limit;//by default will be set to 1


}
