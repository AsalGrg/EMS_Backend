package com.backend.serviceImpls;

import com.backend.dtos.internals.EmailVerificationServiceResponse;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.EmailVerification;
import com.backend.models.User;
import com.backend.repositories.EmailVerificationRepository;
import com.backend.services.EmailVerificationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private EmailVerificationRepository emailVerificationRepo;
    private PasswordEncoder otpEncoder;

    public EmailVerificationServiceImpl(
            PasswordEncoder passwordEncoder,
            EmailVerificationRepository emailVerificationRepo
    ){
        this.otpEncoder= passwordEncoder;
        this.emailVerificationRepo= emailVerificationRepo;
    }
    @Override
    public EmailVerificationServiceResponse getEmailVerification(User user) {

        Random random= new Random();
        String otp=  String.valueOf(random.nextInt(99999));

        String verificationToken = String.valueOf(UUID.randomUUID());

        EmailVerification emailVerification= new EmailVerification (
            otpEncoder.encode(otp),
            verificationToken,
                LocalDateTime.now(),
                user
        );

        emailVerificationRepo.saveEmailVerification(emailVerification);

        return EmailVerificationServiceResponse.builder()
                .otp(otp)
                .verificationToken(verificationToken)
                .build();
    }

    public EmailVerification validateOtp(String verificationToken, String otp){
        EmailVerification emailVerification= getEmailVerificationByToken(verificationToken);

        boolean otpMatches= otpEncoder.matches(otp, emailVerification.getOtp());

        if(!otpMatches) throw  new IllegalStateException("Invalid OTP code!");

        if(emailVerification.getUser().isVerified() || emailVerification.getVerifiedAt()!=null){
            throw new IllegalStateException("User already verified!");
        }

        if(LocalDateTime.now().isAfter(emailVerification.getSentAt().plusMinutes(2))){
            throw new IllegalStateException("OTP already expired");
        }

        emailVerification.setVerifiedAt(LocalDateTime.now());
        emailVerificationRepo.saveEmailVerification(emailVerification);

        return emailVerification;
    }

    private EmailVerification getEmailVerificationByToken(String token){
        return emailVerificationRepo.getByEmailVerificationToken(token)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Verification Token"));
    }
}
