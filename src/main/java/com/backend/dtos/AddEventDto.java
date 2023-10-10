package com.backend.dtos;


import com.backend.models.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AddEventDto {


    @NotNull(message = "Event name must be given")
    @NotEmpty(message = "Event name cannot be empty")
    private String name;

    @NotNull(message = "Event name must be given")
    @NotEmpty(message = "Event name cannot be empty")
    private String location;


    @NotNull(message = "Event date must be given")
    private LocalDate eventDate;


    private LocalDate publishedDate;

    @NotNull(message = "Event description must be given")
    @NotEmpty(message = "Event description cannot be empty")
    private String description;

    @NotNull(message = "Event entry fee must be given")
    private double entryFee;

    private boolean isPrivate;//by default the value is false

    @NotNull(message = "Available seats must be given")
    private int seats;

    private List<String> event_group;

    @NotNull(message = "Event name must be given")
    @NotEmpty(message = "Event name cannot be empty")
    private String eventType;


}
