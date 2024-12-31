package com.software.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, String signUpToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        String subject = "JOB_HUNT Email Confirmation";
        String body = "Please click the link below to confirm your email address in 5 minutes:\n"
                + "http://localhost:8080/auth/confirm-email?token=" + signUpToken;
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendResetEmail(String to, String resetPasswordToken) {
        System.out.println("Sending email to: " + to);
        SimpleMailMessage message = new SimpleMailMessage();
        String subject = "JOB_HUNT Password Reset";
        String body = "Click the following link to reset your password in 5 minutes:\n"
                + "http://localhost:5173/reset-password?token=" + resetPasswordToken;
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        System.out.println("Email sent");

    }

    public void sendApplicationSubmitConfirmationEmail(String to){
        SimpleMailMessage message = new SimpleMailMessage();
        String subject = "JOB_HUNT Application Submission Confirmation";
        String body = "Your application has been submitted successfully. You will be notified once the company has reviewed your application.";
        //need to add application details or job details , maybe a link to the application or job
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendApplicationAcceptanceEmail(String to){
        SimpleMailMessage message = new SimpleMailMessage();
        String subject = "JOB_HUNT Application Acceptance";
        String body = "Congratulations! Your application has been accepted. You will be contacted by the company soon.";
        //need to add job details , maybe a link to the job
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendApplicationRejectionEmail(String to){
        SimpleMailMessage message = new SimpleMailMessage();
        String subject = "JOB_HUNT Application Rejection";
        String body = "We regret to inform you that your application has been rejected. Please keep applying to other jobs.";
        //need to add job details , maybe a link to the job
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

}
