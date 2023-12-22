package com.backend.dtos;

import com.backend.utils.IsImage;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter

@Validated

public class AddStarringDto {

    @IsImage
    private MultipartFile starringPhoto;

    @NotNull(message = "Starring name is required")
    @NotEmpty(message = "Starring name cannot be empty")
    private String starringName;
}
