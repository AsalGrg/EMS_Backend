package com.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class ApplyPromoCodeResponseDto {
    private String promoCode;
    private double netTotal;
    private double grandTotal; //after promoCode applied
    private double discountAmount;//-200 saved
    private String discountType;//10% off or 200 off
    private LocalDate validTill;
}
