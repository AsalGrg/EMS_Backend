package com.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ApplyPromoCodeResponseDto {

    private double netTotal;
    private double grandTotal; //after promoCode applied
    private double discountAmount;//-200 saved
    private String discountType;//10% off or 200 off
}
