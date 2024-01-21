package com.backend.dtos.addEvent;


import com.backend.dtos.AddStarringDto;
import com.backend.models.PromoCode;
import com.backend.utils.ConditionalNotNull;
import com.backend.utils.IsImage;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Validated
public class AddEventRequestDto {

//    @IsImage(message = "Valid cover image is required!")
    private MultipartFile eventCoverPhoto;

    @NotNull(message = "Event name must be given")
    @NotEmpty(message = "Event name cannot be empty")
    private String eventName;

    @NotNull(message = "Event category is required")
    @NotEmpty(message = "Event category cannot be empty")
    private String eventCategory;

    @NotNull(message = "Event location type is required")
    private String locationType;

    private String locationName;

//
//    @NotNull(message = "Event date details are required")
//    @NotNull(message = "Event date details are required")
    private EventDateDetailsDto eventDateDetails;

    private EventTicketDetailsDto eventTicketDetails;


    @NotNull(message = "About event is required")
    @NotEmpty(message = "About event cannot be empty")
    private String aboutEvent;

    private boolean hasStarring;

    private List<MultipartFile> starringImages;

    private List<String> starringNames;

    private boolean isPrivate;

    @NotNull(message = "Event visibility type is required")
    @NotEmpty(message = "Event visibility type cannot be empty")
    private String eventVisibilityType;

    private String eventAccessPassword;

}
