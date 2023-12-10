package com.backend.dtos.register;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyOtpRequest
{
    @NotNull(message = "OTP cannot be null")
    @NotEmpty(message = "OTP cannot be empty")
    @Size(max = 5, min = 5, message = "Enter a 5 digit valid OTP")
    String otp;

    @NotEmpty(message = "Verification token is required")
    @NotNull(message = "Verification token is must")
    String verificationToken;
}
