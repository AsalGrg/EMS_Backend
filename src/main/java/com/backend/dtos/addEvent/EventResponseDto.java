package com.backend.dtos.addEvent;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class EventResponseDto {

    private int eventId;
    private int pageStatus;
    private String eventStatus;
    private String eventCoverImgUrl;
    private String eventName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalDate ticketSalesEndDate;
    private String category;
    private String location_display_name;
    private double lat;
    private double lon;
    private String country;
    private String ticketType;
    private Double ticketPrice;
    private Integer ticketsForSale;
    private Integer ticketsSold;
    private String organizerName;
}

