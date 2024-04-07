package com.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class VendorResponseDto {

    private int vendorId;
    private String vendorName;
    private String vendorProfile;
    private int vendorFollowers;
    private boolean hasFollowed;
}
