package com.backend.dtos.aboutEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TicketDetail {
    private int ticketBookedQuantity;
    private String ticketName;
    private double ticketPrice;
    private String ticketType;
}
