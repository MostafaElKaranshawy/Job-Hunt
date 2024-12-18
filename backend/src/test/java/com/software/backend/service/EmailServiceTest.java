package com.software.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private String email;
    private String signUpToken;
    private String resetPasswordToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        email = "test@gmail.com";
        signUpToken = "sampleSignUpToken";
        resetPasswordToken = "sampleResetPasswordToken";
    }

    @Test
    void testSendConfirmationEmail_shouldSendEmailWithCorrectSubjectAndBody() {
        // Arrange
        SimpleMailMessage message = new SimpleMailMessage();
        String expectedBody = "Please click the link below to confirm your email address in 5 minutes:\n"
                + "http://localhost:8080/auth/confirm-email?token=" + signUpToken;
        message.setTo(email);
        message.setSubject("JOB_HUNT Email Confirmation");
        message.setText(expectedBody);

        emailService.sendConfirmationEmail(email, signUpToken);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));  // Verify mailSender is called
        verifyMailMessage(message);
    }

    @Test
    void testSendResetEmail_shouldSendEmailWithCorrectSubjectAndBody() {
        SimpleMailMessage message = new SimpleMailMessage();
        String expectedBody = "Click the following link to reset your password in 5 minutes:\n"
                + "http://localhost:5173/reset-password?token=" + resetPasswordToken;
        message.setTo(email);
        message.setSubject("JOB_HUNT Password Reset");
        message.setText(expectedBody);

        emailService.sendResetEmail(email, resetPasswordToken);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));  // Verify mailSender is called
        verifyMailMessage(message);
    }

    private void verifyMailMessage(SimpleMailMessage expectedMessage) {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage sentMessage = captor.getValue();

        assertNotNull(sentMessage);
        assertEquals(expectedMessage.getTo()[0], sentMessage.getTo()[0]);
        assertEquals(expectedMessage.getSubject(), sentMessage.getSubject());
        assertEquals(expectedMessage.getText(), sentMessage.getText());
    }
}
