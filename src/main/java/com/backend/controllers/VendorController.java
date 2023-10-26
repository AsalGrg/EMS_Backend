package com.backend.controllers;

import com.backend.dtos.vendorRegistration.VendorRegistrationRequestDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationResponse;
import com.backend.repositories.EventAccessRequestRepository;
import com.backend.serviceImpls.EventServiceImplementation;
import com.backend.serviceImpls.VendorCredentialServiceImplementation;
import com.backend.utils.FileNotEmpty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@Validated
//@RequestMapping("/vendor")
public class VendorController {

    private VendorCredentialServiceImplementation vendorCredentialServiceImpl;

    private EventServiceImplementation eventServiceImpl;

    private HttpSession httpSession;

    private ObjectMapper objectMapper;

    public VendorController
            (VendorCredentialServiceImplementation vendorCredentialServiceImpl,HttpSession httpSession, EventServiceImplementation eventServiceImpl
            ,ObjectMapper objectMapper){
        this.vendorCredentialServiceImpl= vendorCredentialServiceImpl;
        this.eventServiceImpl= eventServiceImpl;
        this.httpSession= httpSession;
        this.objectMapper= objectMapper;
    }


    @PostMapping("/become-a-vendor")
//    public ResponseEntity<?> becomeVendor(@Valid @RequestBody VendorRegistrationRequestDto vendorRegistrationReq)

    public ResponseEntity<?> becomeVendor(@Valid @FileNotEmpty(value=MediaType.IMAGE_JPEG_VALUE) @RequestParam("taxClearanceFile") MultipartFile taxClearanceFile){

//        vendorRegistrationReq.setUsername((String) httpSession.getAttribute("CurrentUser"));
//        VendorRegistrationResponse registrationResponse= this.vendorCredentialServiceImpl.becomeVendor(vendorRegistrationReq);

//        return new ResponseEntity<>(registrationResponse, HttpStatus.OK);

//            VendorRegistrationRequestDto vendorRegistrationRequestDto= objectMapper.readValue(vendorData, VendorRegistrationRequestDto.class);
            VendorRegistrationRequestDto vendorRegistrationRequestDto= new VendorRegistrationRequestDto();
            vendorRegistrationRequestDto.setTaxClearanceCertificate(taxClearanceFile);
            vendorRegistrationRequestDto.setUsername((String) httpSession.getAttribute("CurrentUser"));

            VendorRegistrationResponse registrationResponse= this.vendorCredentialServiceImpl.becomeVendor(vendorRegistrationRequestDto);
            return new ResponseEntity<>(registrationResponse, HttpStatus.OK);


    }

    @PostMapping("/addEvent-request-action/{eventName}/{action}")
    public ResponseEntity<?> addEventVendorRequestAction(@PathVariable("eventName") String eventName, @PathVariable("action")String action){
        String username = (String)httpSession.getAttribute("CurrentUser");

        this.eventServiceImpl.addEventVendorRequestAction(username, action, eventName);

        return new ResponseEntity<>("Add request event "+action+"ed successfully", HttpStatus.OK);
    }
}
