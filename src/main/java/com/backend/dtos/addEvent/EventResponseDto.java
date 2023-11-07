package com.backend.dtos.addEvent;


import java.time.LocalDate;

public record EventResponseDto(String accessToken, String eventName, String location, LocalDate publishedDate,
                               LocalDate eventDate, double entryFee, String eventType) {

}
