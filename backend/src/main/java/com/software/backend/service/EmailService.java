package com.software.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private String subject = "JOB_HUNT Email Confirmation";
    public void sendEmail(String to, String signUpToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        String body = "Please click the link below to confirm your email address:\n"
                + "http://localhost:8080/auth/confirm-email?token=" + signUpToken;
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
