package com.backend.dtos.addEvent;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder


public class AddEventFourthPageDto {

    @NotNull(message = "Event Id is required")
    private Integer eventId;

    @NotNull(message = "Event visibility type is required")
    @NotEmpty(message = "Event visibility type cannot be empty")
    private String visibilityOption;

    private String accessPassword;
}
