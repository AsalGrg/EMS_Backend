package com.backend.dtos.vendorRegistration;

import com.backend.utils.FileNotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter

public class VendorRegistrationRequestDto {
    private String username;

//    @FileNotEmpty(value=MediaType.APPLICATION_PDF_VALUE, message = "Please note that we require a Tax Clearance Document in PDF format")
    private MultipartFile taxClearanceCertificate;

//    @FileNotEmpty(value=MediaType.APPLICATION_PDF_VALUE, message = "Please note that we require a Business Registration Document in PDF format")
    private MultipartFile vendorRegistrationDocument;//license

//    @FileNotEmpty(value=MediaType.APPLICATION_PDF_VALUE, message = "Please note that we require a Filled Vendor Registration Form in PDF format")
    private MultipartFile vendorRegistrationFilledForm;

    @NotNull(message = "Please describe something about your vendor")
    private String vendorDescription;

    @NotNull(message = "A business email is required")
    private String businessEmail;

    @NotNull(message = "Social media links are must")
    @Size(min = 4, max = 4, message = "social media links must contain 4 links")
    private List<String> socialMediaLinks;//first:facebook, second:instagram, third:Tiktok and last:LinkedIn

    @NotNull(message = "Business contacts are required")
    @Size(min = 2, max = 2, message = "Only and max 2 business contacts are accepted")
    private List<String> contacts;


}
