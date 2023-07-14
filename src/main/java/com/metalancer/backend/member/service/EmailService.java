package com.metalancer.backend.member.service;

public interface EmailService {
    public void sendEmail(String to, String subject, String text);
}
