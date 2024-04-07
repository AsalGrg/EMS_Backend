package com.backend.dtos.aboutEvent;

import com.backend.dtos.VendorResponseDto;
import com.backend.models.EventPhysicalLocationDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class EventDescriptionResponseDto {
    private int eventId;
    private String aboutEvent;
    private String coverImage;
    private LocalDate eventStartDate;
    private LocalDate eventEndDate;
    private LocalTime eventStartTime;
    private LocalTime eventEndTime;
    private String eventTitle;
    private boolean hasStarring;
    private String locationType;
    private EventPhysicalLocationDetails physicalLocationDetails;
    private List<EachStarring> starrings;
    private TicketDetail ticketDetails;
    private VendorResponseDto vendorDetails;
    private boolean hasLiked;
    private String ticketType;
    private Double ticketPrice;
}
