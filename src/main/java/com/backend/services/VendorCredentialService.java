package com.backend.services;

import com.backend.dtos.VendorRequestsDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationRequestDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationResponse;
import com.backend.models.VendorCredential;

import java.util.List;

public interface VendorCredentialService {

    VendorRegistrationResponse becomeVendor(VendorRegistrationRequestDto vendorRegistrationReq);

    List<VendorRequestsDto> getVendorRequests(String username);

    void vendorRequestAction(String username, String vendorName, String action);
}
