package com.backend.dtos.draftDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EventDraftDetails {

    private int eventId;
    private String eventTitle;
    private String category;
    private String venueType;
    private String location;
    private String meetingLink;
    private List<LocalDate> eventDates;
    private LocalTime eventStartTime;
    private LocalTime eventEndTime;
    private boolean displayStartTime;
    private boolean displayEndTime;
    private String coverImage;
    private boolean isAboutClicked;
    private String aboutEvent;
    private boolean hasStarring;
    private List<EachStarringDetail> starrings;
    private String ticketType;
    private String ticketName;
    private Double ticketPrice;
    private Integer ticketQuantity;
    private List<LocalDate> ticketSaleDates;
    private LocalTime saleStartTime;
    private LocalTime saleEndTime;
    private boolean isPrivate;
    private String visibilityOption;
    private String accessPassword;
    private SelectedPlaceDetails selectedPlace;
    private int active;
    private Object matchedPlaces;
}
