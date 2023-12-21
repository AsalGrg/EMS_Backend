package com.backend.controllers;

import com.backend.dtos.VendorDetailViewDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationRequestDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationResponse;
import com.backend.services.EventService;
import com.backend.services.VendorCredentialService;
import com.backend.utils.FileNotEmpty;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin("*")

@Validated
//@RequestMapping("/vendor")
public class VendorController {

    private final VendorCredentialService vendorCredentialService;

//    private final EventService eventService;

    private final HttpSession httpSession;

//    private final ObjectMapper objectMapper;

    Logger logger= LoggerFactory.getLogger(VendorController.class);

    public VendorController
            (VendorCredentialService vendorCredentialService,HttpSession httpSession){
        this.vendorCredentialService= vendorCredentialService;
//        this.eventService= eventService;
        this.httpSession= httpSession;
    }

    @GetMapping("/allVendors")
    public ResponseEntity<?> getAllVendors(){
        List<VendorDetailViewDto> allVendorsView= vendorCredentialService.getAllVendors();

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
        VendorRegistrationResponse vendorRegistrationResponse = vendorCredentialService.becomeVendor(vendorRegistrationRequestDto);

        return ResponseEntity.ok(vendorRegistrationResponse);
    }

    @PostMapping("/addEvent-request-action/{eventName}/{action}")
    public ResponseEntity<?> addEventVendorRequestAction(@PathVariable("eventName") String eventName, @PathVariable("action")String action){
        String username = (String)httpSession.getAttribute("CurrentUser");

//        eventService.addEventVendorRequestAction(username, action, eventName);

        return new ResponseEntity<>("Add request event "+action+"ed successfully", HttpStatus.OK);
    }
}
