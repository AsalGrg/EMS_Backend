package com.backend.dtos.internals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class EmailVerificationServiceResponse {

    String otp;
    String verificationToken;
}
