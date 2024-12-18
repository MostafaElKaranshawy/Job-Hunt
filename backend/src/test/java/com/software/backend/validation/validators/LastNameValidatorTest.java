package com.software.backend.validation.validators;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LastNameValidatorTest {

    private LastNameValidator lastNameValidator;

    @BeforeEach
    void setUp() {
        lastNameValidator = new LastNameValidator();
    }

    @Test
    public void shouldThrowExceptionWhenLastNameIsNull() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setLastName(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> lastNameValidator.doValidation(signUpRequest));

        assertEquals("Last name is required", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenLastNameIsEmpty() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setLastName("");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> lastNameValidator.doValidation(signUpRequest));

        assertEquals("Last name is required", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenLastNameExceedsMaxLength() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setLastName("This is a very long last name that exceeds the maximum length");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> lastNameValidator.doValidation(signUpRequest));

        assertEquals("Last name must not exceed 30 characters", exception.getMessage());
    }

    @Test
    public void shouldPassValidationWhenLastNameIsValid() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setLastName("Mahmoud");

        // Act & Assert
        assertDoesNotThrow(() -> lastNameValidator.doValidation(signUpRequest));
    }

}