package com.backend.dtos.vendorRegistration;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorRegistrationResponse {
    private String message;

    public VendorRegistrationResponse(){}

    public VendorRegistrationResponse(String message){
        this.message= message;
    }
}
