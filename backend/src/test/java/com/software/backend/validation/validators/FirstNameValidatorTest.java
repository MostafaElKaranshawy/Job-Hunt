package com.software.backend.validation.validators;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FirstNameValidatorTest {

    private FirstNameValidator firstNameValidator;

    @BeforeEach
    void setUp() {
        firstNameValidator = new FirstNameValidator();
    }

    @Test
    public void shouldThrowExceptionWhenFirstNameIsNull() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> firstNameValidator.doValidation(signUpRequest));

        assertEquals("First name is required", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFirstNameIsEmpty() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> firstNameValidator.doValidation(signUpRequest));

        assertEquals("First name is required", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFirstNameExceedsMaxLength() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("This is a very long first name that exceeds the maximum length");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> firstNameValidator.doValidation(signUpRequest));

        assertEquals("First name must not exceed 30 characters", exception.getMessage());
    }

    @Test
    public void shouldPassValidationWhenFirstNameIsValid() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("Youssef");

        // Act & Assert
        assertDoesNotThrow(() -> firstNameValidator.doValidation(signUpRequest));
    }

}
