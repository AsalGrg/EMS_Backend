package com.backend.services;

import java.util.List;

public interface EmailService {

    public void sendEmail(String receiver,String subject, String body);
}
