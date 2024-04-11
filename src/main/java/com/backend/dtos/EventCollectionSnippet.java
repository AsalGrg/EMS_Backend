package com.backend.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class EventCollectionSnippet {

    @NotNull(message = "Collection name is required")
    @NotEmpty(message = "Collection name cannot be empty")
    private String collectionName;


    private int noOfUpcomingEvents;

    @NotNull(message = "Collection description is required")
    @NotEmpty(message = "Collection description cannot be empty")
    private String description;
    private Object coverImg;
    private LocalDate lastAccessedDate;
    private LocalTime lastAccessedTime;

}
