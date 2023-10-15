package com.backend.controllers;

import com.backend.dtos.vendorRegistration.VendorRegistrationRequestDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationResponse;
import com.backend.serviceImpls.VendorCredentialServiceImplementation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    private VendorCredentialServiceImplementation vendorCredentialServiceImpl;

    public VendorController(VendorCredentialServiceImplementation vendorCredentialServiceImpl){
        this.vendorCredentialServiceImpl= vendorCredentialServiceImpl;
    }


    @PostMapping("/become-a-vendor")
    public ResponseEntity<?> becomeVendor(@Valid @RequestBody VendorRegistrationRequestDto vendorRegistrationReq, HttpSession httpSession){
        vendorRegistrationReq.setUsername((String) httpSession.getAttribute("CurrentUser"));
        VendorRegistrationResponse registrationResponse= this.vendorCredentialServiceImpl.becomeVendor(vendorRegistrationReq);

        return new ResponseEntity<>(registrationResponse, HttpStatus.OK);
    }
}
