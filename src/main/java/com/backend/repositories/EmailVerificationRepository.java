package com.backend.repositories;


import com.backend.models.EmailVerification;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface EmailVerificationRepository {

//    Optional<EmailVerification> findByVerificationToken(String verificationToken);

    Optional<EmailVerification> getByEmailVerificationToken(String token);

    void saveEmailVerification(EmailVerification emailVerification);
}
