package com.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class LoginRegisterResponse {

    String message;
    String jwtToken;
    LocalDateTime timeStamp;
//    String
}
