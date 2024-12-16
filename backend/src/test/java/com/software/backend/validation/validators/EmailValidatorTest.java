package com.software.backend.validation.validators;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsNull() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> emailValidator.doValidation(signUpRequest));

        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsEmpty() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> emailValidator.doValidation(signUpRequest));

        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsNotGmail() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("example@yahoo.com");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> emailValidator.doValidation(signUpRequest));

        assertEquals("Email must be a valid email address", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsInvalidFormat() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("@gmail.com");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> emailValidator.doValidation(signUpRequest));

        assertEquals("Email must be a valid email address", exception.getMessage());
    }

    @Test
    public void shouldPassValidationForValidGmailEmail() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("example@gmail.com");

        // Act & Assert
        assertDoesNotThrow(() -> emailValidator.doValidation(signUpRequest));
    }
}
