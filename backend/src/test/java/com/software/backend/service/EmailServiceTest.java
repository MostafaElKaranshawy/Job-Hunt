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
    private String jobTitle;
    private String companyName;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        email = "test@gmail.com";
        signUpToken = "sampleSignUpToken";
        resetPasswordToken = "sampleResetPasswordToken";
        jobTitle = "Software Engineer";
        companyName = "TechCorp";
    }

    @Test
    void testSendConfirmationEmail_shouldSendEmailWithCorrectSubjectAndBody() {
        SimpleMailMessage message = new SimpleMailMessage();
        String expectedBody = "Please click the link below to confirm your email address in 5 minutes:\n"
                + "http://localhost:8080/auth/confirm-email?token=" + signUpToken;
        message.setTo(email);
        message.setSubject("JOB_HUNT Email Confirmation");
        message.setText(expectedBody);

        emailService.sendConfirmationEmail(email, signUpToken);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
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

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verifyMailMessage(message);
    }

    @Test
    void testSendApplicationSubmitConfirmationEmail_shouldSendEmailWithCorrectSubjectAndBody() {
        SimpleMailMessage message = new SimpleMailMessage();
        String expectedBody = "Your application has been submitted successfully. You will be notified once the company has reviewed your application.";
        message.setTo(email);
        message.setSubject("JOB_HUNT Application Submission Confirmation");
        message.setText(expectedBody);

        emailService.sendApplicationSubmitConfirmationEmail(email, jobTitle, companyName);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verifyMailMessage(message);
    }

    @Test
    void testSendApplicationAcceptanceEmail_shouldSendEmailWithCorrectSubjectAndBody() {
        SimpleMailMessage message = new SimpleMailMessage();
        String expectedBody = "Congratulations! Your application for the position of " + jobTitle + " at " + companyName + " has been accepted. The HR team will contact you shortly with further details and next steps. Please feel free to reach out if you have any questions in the meantime.";
        message.setTo(email);
        message.setSubject("JOB_HUNT Application Acceptance");
        message.setText(expectedBody);

        emailService.sendApplicationAcceptanceEmail(email, jobTitle, companyName);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verifyMailMessage(message);
    }

    @Test
    void testSendApplicationRejectionEmail_shouldSendEmailWithCorrectSubjectAndBody() {
        SimpleMailMessage message = new SimpleMailMessage();
        String expectedBody = "We regret to inform you that your application for the position of " + jobTitle + " at " + companyName + " has been rejected. We appreciate the time and effort you put into applying and wish you the very best in your future endeavors. Please feel free to apply for other opportunities with us in the future.";
        message.setTo(email);
        message.setSubject("JOB_HUNT Application Rejection");
        message.setText(expectedBody);

        emailService.sendApplicationRejectionEmail(email, jobTitle, companyName);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
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
