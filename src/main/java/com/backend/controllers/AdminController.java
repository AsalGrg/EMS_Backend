package com.backend.controllers;


import com.backend.dtos.VendorRequestsDto;
import com.backend.models.User;
import com.backend.models.VendorCredential;
import com.backend.serviceImpls.VendorCredentialServiceImplementation;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private VendorCredentialServiceImplementation vendorCredentialServiceImplementation;

    public AdminController(VendorCredentialServiceImplementation vendorCredentialServiceImplementation){
        this.vendorCredentialServiceImplementation= vendorCredentialServiceImplementation;
    }

    @GetMapping("/become-vendor-requests")
    public ResponseEntity<?> getVendorRequests(HttpSession httpSession){


        //here the username is of the current user to check its credentials
        String username= (String) httpSession.getAttribute("CurrentUser");

        List<VendorRequestsDto> vendorRequests= this.vendorCredentialServiceImplementation.getVendorRequests(username);

        return new ResponseEntity<>(vendorRequests, HttpStatus.OK);
    }


}
