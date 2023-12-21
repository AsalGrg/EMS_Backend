package com.backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter

public class SearchEventByFilterDto {

    @NotNull(message = "Please fill out location")
    private String location;

    @NotNull(message = "Please fill out date")
    private LocalDate event_date;

    @NotNull(message = "Please fill out time")
    private LocalTime event_time;

    @NotNull(message = "Please fill out event category")
    private String event_category;

}
