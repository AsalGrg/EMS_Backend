package com.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorRequestsDto {

    private int credential_id;
    private String username;
    private String email;
    private String address;

    private boolean verified;
    private boolean declined;
}
