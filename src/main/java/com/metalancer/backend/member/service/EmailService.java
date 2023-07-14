package com.metalancer.backend.member.service;

import jakarta.mail.MessagingException;

import java.util.HashMap;

public interface EmailService {
    public void sendEmail(String to, String subject, String text);

    public void sendMail(String to, String title, String templateName,
                         HashMap<String, String> values) throws MessagingException;
}
