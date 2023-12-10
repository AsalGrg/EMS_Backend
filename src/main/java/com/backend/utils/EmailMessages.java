package com.backend.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailMessages {

    public Map<String, String> vendorAcceptanceMessage(String username){

        Map<String, String> vendorSuccessSubjectAndMessage= new HashMap<>();

        vendorSuccessSubjectAndMessage.put("subject", "Vendor Account Request Approval");
        vendorSuccessSubjectAndMessage.put("message",  "Dear "+username+",\n" +
                "\n" +
                "Your vendor account request has been approved. You can now log in and start using our platform. For any assistance, our support team is here to help.\n" +
                "\n" +
                "Thank you for choosing us.\n" +
                "\n" +
                "Best regards,\n" +
                "The Prastuti Team");

        return vendorSuccessSubjectAndMessage;
    }

    public Map<String, String> vendorDeclinedMessage(String username){

        Map<String, String> vendorDeclineSubjectAndMessage= new HashMap<>();

        vendorDeclineSubjectAndMessage.put("subject", "Vendor Account Request Declined");
        vendorDeclineSubjectAndMessage.put("message","Dear "+username+",\n" +
                "\n" +
                "Your vendor account request has been approved. You can now log in and start using our platform. For any assistance, our support team is here to help.\n" +
                "\n" +
                "Thank you for choosing us.\n" +
                "\n" +
                "Best regards,\n" +
                "The Prastuti Team");

        return vendorDeclineSubjectAndMessage;
    }

    public Map<String, String> userEmailVerificationOtpMessage(String username, String otp){

        Map<String, String> vendorSuccessSubjectAndMessage= new HashMap<>();

        vendorSuccessSubjectAndMessage.put("subject", "Email Verification OTP");
        vendorSuccessSubjectAndMessage.put("message",  "Dear "+username+",\n" +
                "\n" +
                "Your OTP code for email verification is: "+otp+".\n" +
                "\n" +
                "Thank you.\n" +
                "\n" +
                "The Prastuti Team");

        return vendorSuccessSubjectAndMessage;
    }

//    public String
}
