package com.backend.controllers;

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
import java.util.Map;

@RestController
@Validated
//@RequestMapping("/vendor")
public class VendorController {

    @Autowired
    private Cloudinary cloudinary;
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


//    @PostMapping("/become-a-vendor")
////    public ResponseEntity<?> becomeVendor(@Valid @RequestBody VendorRegistrationRequestDto vendorRegistrationReq)
//
//    public ResponseEntity<?> becomeVendor(@Valid @FileNotEmpty(value=MediaType.APPLICATION_PDF_VALUE, message = "Please note that we require a Tax Clearance Document in PDF format") @RequestParam("taxClearanceFile") MultipartFile taxClearanceFile,
//                                          @Valid @FileNotEmpty(value=MediaType.APPLICATION_PDF_VALUE, message = "Please note that we require a Business Registration Document in PDF format") @RequestParam("vendorRegistrationDocument") MultipartFile vendorRegistrationDocument,
//                                          @Valid @FileNotEmpty(value=MediaType.APPLICATION_PDF_VALUE, message = "Please note that we require a Filled Vendor Registration Form in PDF format") @RequestParam("vendorRegistrationFilledForm") MultipartFile vendorRegistrationFilledForm,
//                                          @RequestParam("vendorData") String vendorData){
//
////        vendorRegistrationReq.setUsername((String) httpSession.getAttribute("CurrentUser"));
////        VendorRegistrationResponse registrationResponse= this.vendorCredentialServiceImpl.becomeVendor(vendorRegistrationReq);
//
////        return new ResponseEntity<>(registrationResponse, HttpStatus.OK);
//
////            VendorRegistrationRequestDto vendorRegistrationRequestDto= objectMapper.readValue(vendorData, VendorRegistrationRequestDto.class);
//        VendorRegistrationRequestDto vendorRegistrationRequestDto= null;
//
//        try {
//            vendorRegistrationRequestDto = objectMapper.readValue(vendorData, VendorRegistrationRequestDto.class);
//            logger.info("Vendor:{}", vendorData);
//
//            logger.info("VendorEmail: {}", vendorRegistrationRequestDto.getBusinessEmail());
//
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Invalid JSON value, error while converting to the JSON");
//        }
//        vendorRegistrationRequestDto.setTaxClearanceCertificate(taxClearanceFile);
//        vendorRegistrationRequestDto.setUsername((String) httpSession.getAttribute("CurrentUser"));
//
//        VendorRegistrationResponse registrationResponse= this.vendorCredentialServiceImpl.becomeVendor(vendorRegistrationRequestDto);
//        return new ResponseEntity<>(registrationResponse, HttpStatus.OK);
//
//
//    }

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
