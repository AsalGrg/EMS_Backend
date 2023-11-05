package com.backend.controllers;


import com.backend.dtos.VendorRequestsDto;
import com.backend.serviceImpls.VendorCredentialServiceImplementation;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
//@RequestMapping("/admin")
public class AdminController {


    private final HttpSession httpSession;

    private final VendorCredentialServiceImplementation vendorCredentialServiceImplementation;

    public AdminController(VendorCredentialServiceImplementation vendorCredentialServiceImplementation,HttpSession httpSession){
        this.vendorCredentialServiceImplementation= vendorCredentialServiceImplementation;
        this.httpSession= httpSession;
    }

    @GetMapping("/become-vendor-requests")
    public ResponseEntity<?> getVendorRequests(){


        //here the username is of the current user to check its credentials
        String username= (String) httpSession.getAttribute("CurrentUser");

        List<VendorRequestsDto> vendorRequests= vendorCredentialServiceImplementation.getVendorRequests(username);

        return new ResponseEntity<>(vendorRequests, HttpStatus.OK);
    }

    @PostMapping("/vendor-request-action/{vendorName}/{action}")
    public ResponseEntity<?> acceptVendorRequest(@PathVariable("vendorName") String vendorName, @PathVariable("action") String action){

        String username= (String) httpSession.getAttribute("CurrentUser");

        //username for checking if the current user is the admin or not
        this.vendorCredentialServiceImplementation.vendorRequestAction(username, vendorName, action);

        return new ResponseEntity<>("Vendor "+action+"d successfully", HttpStatus.OK);
    }

    @PostMapping("/terminatedVendors")
    public ResponseEntity<?> getTerminatedVendors(){

        String username = (String) httpSession.getAttribute("CurrentUser");

        List<VendorRequestsDto> terminatedVendors = vendorCredentialServiceImplementation.getTerminatedVendors(username);

        return new ResponseEntity<>(terminatedVendors, HttpStatus.OK);
    }
}
