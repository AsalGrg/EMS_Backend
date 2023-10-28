package com.backend.controllers;

import com.backend.dtos.VendorDetailViewDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationRequestDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationResponse;
import com.backend.repositories.EventAccessRequestRepository;
import com.backend.serviceImpls.EventServiceImplementation;
import com.backend.serviceImpls.VendorCredentialServiceImplementation;
import com.backend.utils.FileNotEmpty;
import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Validated
//@RequestMapping("/vendor")
public class VendorController {

    private VendorCredentialServiceImplementation vendorCredentialServiceImpl;

    private EventServiceImplementation eventServiceImpl;

    private HttpSession httpSession;

    private ObjectMapper objectMapper;

    Logger logger= LoggerFactory.getLogger(VendorController.class);

    public VendorController
            (VendorCredentialServiceImplementation vendorCredentialServiceImpl,HttpSession httpSession, EventServiceImplementation eventServiceImpl
            ,ObjectMapper objectMapper){
        this.vendorCredentialServiceImpl= vendorCredentialServiceImpl;
        this.eventServiceImpl= eventServiceImpl;
        this.httpSession= httpSession;
        this.objectMapper= objectMapper;
    }

    @GetMapping("/allVendors")
    public ResponseEntity<?> getAllVendors(){
        List<VendorDetailViewDto> allVendorsView= this.vendorCredentialServiceImpl.getAllVendors();

        return ResponseEntity.ok(allVendorsView);
    }

    @PostMapping("/become-a-vendor")
    public ResponseEntity<?> becomeVendor(@RequestPart("vendorData") @Valid VendorRegistrationRequestDto vendorRegistrationRequestDto,
                                         @RequestPart("taxClearanceFile") @FileNotEmpty(value= MediaType.APPLICATION_PDF_VALUE , message = "Please note that we require a Tax Clearance Document in PDF format") MultipartFile taxClearanceFile,
                                          @RequestPart("vendorRegistrationDocument") @FileNotEmpty(value= MediaType.APPLICATION_PDF_VALUE , message = "Please note that we require a Business Registration Document in PDF format") MultipartFile vendorRegistrationDocument,
                                          @RequestPart("vendorRegistrationFilledForm") @FileNotEmpty(value= MediaType.APPLICATION_PDF_VALUE , message = "Please note that we require a Filled Vendor Registration Form in PDF format") MultipartFile vendorRegistrationFilledForm
    ){

        vendorRegistrationRequestDto.setUsername((String) this.httpSession.getAttribute("CurrentUser"));

        //setting the document files in the dto as:
        vendorRegistrationRequestDto.setTaxClearanceCertificate(taxClearanceFile);
        vendorRegistrationRequestDto.setVendorRegistrationDocument(vendorRegistrationDocument);
        vendorRegistrationRequestDto.setVendorRegistrationFilledForm(vendorRegistrationFilledForm);

        //now passing the vendor request dto to the becomeVendor service method as
        VendorRegistrationResponse vendorRegistrationResponse = this.vendorCredentialServiceImpl.becomeVendor(vendorRegistrationRequestDto);

        return ResponseEntity.ok(vendorRegistrationResponse);
    }

    @PostMapping("/addEvent-request-action/{eventName}/{action}")
    public ResponseEntity<?> addEventVendorRequestAction(@PathVariable("eventName") String eventName, @PathVariable("action")String action){
        String username = (String)httpSession.getAttribute("CurrentUser");

        this.eventServiceImpl.addEventVendorRequestAction(username, action, eventName);

        return new ResponseEntity<>("Add request event "+action+"ed successfully", HttpStatus.OK);
    }
}
