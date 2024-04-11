package com.backend.dtos.addEvent;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder

public class AddEventSecondPageDto {

    @NotNull(message = "Event Id is required")
    private Integer eventId;

    private Object eventCoverImage;

    @NotNull(message = "About event cannot be null")
    @NotEmpty(message = "About event cannot be empty")
    private String aboutEvent;

    private boolean hasStarring;
}
