package com.backend.dtos.addEvent;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EventResponseDto {

    private final String accessToken;
    private final String eventName;
    private final LocalDate publishedDate;
    private final LocalDate eventDate;
    private final String location;
    private final double entryFee;
    private final String eventType;

    public EventResponseDto(String accessToken, String eventName, String location, LocalDate publishedDate, LocalDate eventDate, double entryFee, String eventType){
        this.accessToken= accessToken;
        this.eventName= eventName;
        this.location= location;
        this.publishedDate= publishedDate;
        this.eventDate= eventDate;
        this.entryFee= entryFee;
        this.eventType= eventType;
    }

}
