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

public class EventDateDetailsDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Event start date is required")
    private LocalDate eventStartDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull(message = "Event start time is required")
    private LocalTime eventStartTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Event end date is required")
    private LocalDate eventEndDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull(message = "Event end time is required")
    private LocalTime eventEndTime;

    private boolean displayStartTime;
    private boolean displayEndTime;
}
