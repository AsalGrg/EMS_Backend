package com.backend.serviceImpls;

import com.backend.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImplementation implements EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String receiver, String subject, String body) {
        SimpleMailMessage mailMessage= new SimpleMailMessage();
        mailMessage.setTo(receiver);
        mailMessage.setFrom("asalgoorong@gmail.com");
        mailMessage.setText(body);
        mailMessage.setSubject(subject);

        this.javaMailSender.send(mailMessage);
    }
}
