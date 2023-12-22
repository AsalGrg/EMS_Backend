package com.backend.dtos.addEvent;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EventTicketDetailsDto {


    @NotNull(message = "Ticket type is required")
    @NotEmpty(message = "Ticket type cannot be empty")
    private String ticketType;

    @NotNull(message = "Ticket name is required")
    @NotEmpty(message = "Ticket name cannot be empty")
    private String ticketName;

    @NotNull(message = "Ticket quantity is required")
    @NotEmpty(message = "Ticket quantity cannot be empty")
    private Integer ticketQuantity;

    @NotNull(message = "Ticket price is required")
    @NotEmpty(message = "Ticket price cannot be empty")
    private Float ticketPrice;

    @NotNull(message = "Sales start date is required")
    @NotEmpty(message = "Sales start date cannot be empty")
    private LocalDate saleStartDate;

    @NotNull(message = "Sales start time is required")
    @NotEmpty(message = "About event cannot be empty")
    private LocalTime saleStartTime;

    @NotNull(message = "Sales end date is required")
    @NotEmpty(message = "Sales end date cannot be empty")
    private LocalDate saleEndDate;

    @NotNull(message = "Sales end time is required")
    @NotEmpty(message = "Sales end time cannot be empty")
    private LocalTime saleEndTime;
}
