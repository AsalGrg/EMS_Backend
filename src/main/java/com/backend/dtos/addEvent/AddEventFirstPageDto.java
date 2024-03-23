package com.backend.dtos.addEvent;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Validated
public class AddEventFirstPageDto {

    private Integer eventId;

    @NotNull(message = "Event name must be given")
    @NotEmpty(message = "Event name cannot be empty")
    private String eventName;

    @NotNull(message = "Event category is required")
    @NotEmpty(message = "Event category cannot be empty")
    private String eventCategory;

    @NotNull(message = "Event location type is required")
    private String locationType;

    private String locationName;

    private boolean isPhysical;
    //
//    @NotNull(message = "Event date details are required")
//    @NotNull(message = "Event date details are required")
    private EventDateDetailsDto eventDateDetails;
}
