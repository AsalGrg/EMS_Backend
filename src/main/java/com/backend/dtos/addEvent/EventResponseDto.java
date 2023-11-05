package com.backend.dtos.addEvent;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public record EventResponseDto(String accessToken, String eventName, String location, LocalDate publishedDate,
                               LocalDate eventDate, double entryFee, String eventType) {

}
