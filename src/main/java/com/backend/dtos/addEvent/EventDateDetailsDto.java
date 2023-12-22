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

public class EventDateDetailsDto {

    @NotNull(message = "Event start date is required")
    @NotEmpty(message = "Event start date cannot be empty")
    private LocalDate eventStartDate;

    @NotNull(message = "Event start time is required")
    @NotEmpty(message = "Event start time cannot be empty")
    private LocalTime eventStartTime;

    @NotNull(message = "Event end date is required")
    @NotEmpty(message = "Event end date cannot be empty")
    private LocalDate eventEndDate;

    @NotNull(message = "Event end time is required")
    @NotEmpty(message = "Event end time cannot be empty")
    private LocalTime eventEndTime;

    private boolean displayStartTime;
    private boolean displayEndTime;
}
