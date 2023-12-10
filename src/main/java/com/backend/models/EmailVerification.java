package com.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter


@Entity
@Table(name = "email_verification")
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String otp;
    String verificationToken;
    LocalDateTime verifiedAt;
    LocalDateTime sentAt;

    @JoinColumn(name = "user_id")
    @ManyToOne
    User user;


    public EmailVerification(
            String otp,
            String verificationToken,
            LocalDateTime sentAt,
            User user
    ) {
        this.otp= otp;
        this.sentAt= sentAt;
        this.verificationToken= verificationToken;
        this.user= user;
    }
    public EmailVerification(){

    }
}