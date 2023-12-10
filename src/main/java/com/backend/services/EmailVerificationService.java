package com.backend.services;

import com.backend.dtos.internals.EmailVerificationServiceResponse;
import com.backend.models.EmailVerification;
import com.backend.models.User;

public interface EmailVerificationService {

    EmailVerificationServiceResponse getEmailVerification(User user);

    EmailVerification validateOtp(String verificationToken, String otp);

}
