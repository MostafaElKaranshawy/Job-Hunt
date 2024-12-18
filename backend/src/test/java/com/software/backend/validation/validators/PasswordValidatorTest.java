package com.software.backend.validation.validators;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
    }

    @Test
    public void shouldThrowExceptionWhenPasswordIsNull() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setPassword(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> passwordValidator.doValidation(signUpRequest));

        assertEquals("Password is required", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenPasswordIsEmpty() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setPassword("");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> passwordValidator.doValidation(signUpRequest));

        assertEquals("Password is required", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenPasswordIsTooShort() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setPassword("Short1!");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> passwordValidator.doValidation(signUpRequest));

        assertEquals("Password must be at least 8 characters long", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenPasswordHasNoUppercase() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setPassword("password1!");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> passwordValidator.doValidation(signUpRequest));

        assertEquals("Password must contain at least one uppercase letter", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenPasswordHasNoLowercase() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setPassword("PASSWORD1!");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> passwordValidator.doValidation(signUpRequest));

        assertEquals("Password must contain at least one lowercase letter", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenPasswordHasNoNumber() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setPassword("Password!");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> passwordValidator.doValidation(signUpRequest));

        assertEquals("Password must contain at least one number", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenPasswordHasNoSpecialCharacter() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setPassword("Password1");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> passwordValidator.doValidation(signUpRequest));

        assertEquals("Password must contain at least one special character", exception.getMessage());
    }

    @Test
    public void shouldPassValidationWhenPasswordIsValid() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setPassword("This is a Valid Password 1!");

        // Act & Assert
        assertDoesNotThrow(() -> passwordValidator.doValidation(signUpRequest));
    }

    @Test
    public void shouldThrowExceptionWhenPasswordIsTooLong() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setPassword("This is a very long password that exceeds the maximum length of 30 characters");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> passwordValidator.doValidation(signUpRequest));

        assertEquals("Password must not exceed 30 characters", exception.getMessage());
    }

}
