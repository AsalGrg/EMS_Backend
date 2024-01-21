package com.backend.dtos.addEvent;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    private Integer ticketQuantity;

    @NotNull(message = "Ticket price is required")
    private Float ticketPrice;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Sales start date is required")
    private LocalDate saleStartDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull(message = "Sales start time is required")
    private LocalTime saleStartTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Sales end date is required")
    private LocalDate saleEndDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull(message = "Sales end time is required")
    private LocalTime saleEndTime;
}
