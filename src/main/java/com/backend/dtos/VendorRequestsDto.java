package com.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorRequestsDto {

    private String vendorName;
    private String businessEmail;
    private String address;
    private String vendorDescription;
    private String vendorRegistrationDocument;
    private String taxClearanceCertificate;
    private String vendorRegistrationFilledForm;

}
