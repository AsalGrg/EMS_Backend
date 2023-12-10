package com.backend.dtos.register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class RegisterResponse {

    String message;
    String verificationToken;
    LocalDateTime timestamp;
    String userEmail;

}
