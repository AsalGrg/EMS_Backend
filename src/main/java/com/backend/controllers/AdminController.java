package com.backend.controllers;


import com.backend.dtos.VendorRequestsDto;
import com.backend.services.VendorCredentialService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class AdminController {


    private final HttpSession httpSession;

    private final VendorCredentialService VendorCredentialService;

    public AdminController(VendorCredentialService VendorCredentialService,HttpSession httpSession){
        this.VendorCredentialService= VendorCredentialService;
        this.httpSession= httpSession;
    }

    @GetMapping("/become-vendor-requests")
    public ResponseEntity<?> getVendorRequests(){


        //here the username is of the current user to check its credentials
        String username= (String) httpSession.getAttribute("CurrentUser");

        List<VendorRequestsDto> vendorRequests= VendorCredentialService.getVendorRequests(username);

        return new ResponseEntity<>(vendorRequests, HttpStatus.OK);
    }

    @PostMapping("/vendor-request-action/{vendorName}/{action}")
    public ResponseEntity<?> acceptVendorRequest(@PathVariable("vendorName") String vendorName, @PathVariable("action") String action){

        String username= (String) httpSession.getAttribute("CurrentUser");

        //username for checking if the current user is the admin or not
        this.VendorCredentialService.vendorRequestAction(username, vendorName, action);

        return new ResponseEntity<>("Vendor "+action+"d successfully", HttpStatus.OK);
    }

    @PostMapping("/terminatedVendors")
    public ResponseEntity<?> getTerminatedVendors(){

        String username = (String) httpSession.getAttribute("CurrentUser");

        List<VendorRequestsDto> terminatedVendors = VendorCredentialService.getTerminatedVendors(username);

        return new ResponseEntity<>(terminatedVendors, HttpStatus.OK);
    }
}
