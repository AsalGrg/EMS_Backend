package com.backend.dtos.addEvent;


import com.backend.models.PromoCode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AddEventRequestDto {


    @NotNull(message = "Event name must be given")
    @NotEmpty(message = "Event name cannot be empty")
    private String eventName;

    private String organizerDetails;

    private String locationType;

    private String locationName;

    private LocalDate eventStartDate;
    private LocalTime eventStartTime;
    private LocalDate eventEndDate;
    private LocalTime eventEndTime;
    private boolean displayStartTime;
    private boolean displayEndTime;

    private String aboutEvent;
    private boolean hasStarring;

//    private String

    private String ticketType;
    private String ticketName;
    private String ticketQuantity;
    private String ticketPrice;
    private LocalDate saleStartDate;
    private LocalTime saleStartTime;
    private LocalDate saleEndDate;
    private LocalTime saleEndTime;

    private String eventAccessType;
    private String eventAccessPassword;




    @NotNull(message = "Event location must be given")
    @NotEmpty(message = "Event location cannot be empty")
    private String location;

    @NotNull(message = "Event date must be given")
    private LocalDate event_date;

    private LocalDate published_date;

    private String event_organizer;


    @NotNull(message = "Event Vendor must be given")
    @NotEmpty(message = "Event Vendor cannot be empty")
    private String event_vendor;

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

    private MultipartFile eventCoverPhoto;

    private List<PromoCode> promoCodes;

}
