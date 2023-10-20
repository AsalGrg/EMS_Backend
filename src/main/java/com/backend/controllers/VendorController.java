package com.backend.controllers;

import com.backend.dtos.vendorRegistration.VendorRegistrationRequestDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationResponse;
import com.backend.repositories.EventAccessRequestRepository;
import com.backend.serviceImpls.EventServiceImplementation;
import com.backend.serviceImpls.VendorCredentialServiceImplementation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/vendor")
public class VendorController {

    private VendorCredentialServiceImplementation vendorCredentialServiceImpl;

    private EventServiceImplementation eventServiceImpl;

    private HttpSession httpSession;

    public VendorController
            (VendorCredentialServiceImplementation vendorCredentialServiceImpl,HttpSession httpSession, EventServiceImplementation eventServiceImpl){
        this.vendorCredentialServiceImpl= vendorCredentialServiceImpl;
        this.eventServiceImpl= eventServiceImpl;
        this.httpSession= httpSession;
    }


    @PostMapping("/become-a-vendor")
    public ResponseEntity<?> becomeVendor(@Valid @RequestBody VendorRegistrationRequestDto vendorRegistrationReq){
        vendorRegistrationReq.setUsername((String) httpSession.getAttribute("CurrentUser"));
        VendorRegistrationResponse registrationResponse= this.vendorCredentialServiceImpl.becomeVendor(vendorRegistrationReq);

        return new ResponseEntity<>(registrationResponse, HttpStatus.OK);
    }

    @PostMapping("/addEvent-request-action/{eventName}/{action}")
    public ResponseEntity<?> addEventVendorRequestAction(@PathVariable("eventName") String eventName, @PathVariable("action")String action){
        String username = (String)httpSession.getAttribute("CurrentUser");

        this.eventServiceImpl.addEventVendorRequestAction(username, action, eventName);

        return new ResponseEntity<>("Add request event "+action+"ed successfully", HttpStatus.OK);
    }
}
