package com.backend.services;

import com.backend.dtos.vendorRegistration.VendorRegistrationRequestDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationResponse;

public interface VendorCredentialService {

    VendorRegistrationResponse becomeVendor(VendorRegistrationRequestDto vendorRegistrationReq);
}
