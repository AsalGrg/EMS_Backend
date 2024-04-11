package com.backend.dtos.aboutEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TicketDetail {
    private int ticketBookedQuantity;
    private String ticketName;
    private double ticketPrice;
    private String ticketType;
    private LocalDate salesEndDate;
    private int ticketAvailableQuantity;
    private int initialTicketQuantity;
    private boolean hasPromoCode;
    private boolean isSoldOut;
}
