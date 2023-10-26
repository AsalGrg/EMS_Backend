package com.backend.dtos.vendorRegistration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter

public class VendorRegistrationRequestDto {
    private String username;

    private MultipartFile taxClearanceCertificate;

}
