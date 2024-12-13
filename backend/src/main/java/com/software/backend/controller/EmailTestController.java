package com.software.backend.controller;
import com.software.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/auth/send-test-email")
    public String sendTestEmail(@RequestParam String to) {
        emailService.sendEmail(to, "Test Email", "This is a test email from your Spring Boot app.");
        return "Test email sent to " + to;
    }
}
