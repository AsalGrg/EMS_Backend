package com.backend.dtos.vendor;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder

public class PromoCodeDetailsDto {
    private int promoCodeId;
    private String promCodeName;
    private LocalDate expiryDate;
    private String merit;
    private String applicableFrom;
    private String available;
    private String limit;
    private boolean isActive;
}
