package com.backend.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class VendorDetailViewDto {

    private String vendorName;
    private String vendorDescription;
    private String facebookLink;
    private String instagramLink;
    private String tiktokLink;
    private String linkedInLink;
    private double ratings;
    private String businessEmail;
    private String contactNumber1;
    private String contactNumber2;
}
