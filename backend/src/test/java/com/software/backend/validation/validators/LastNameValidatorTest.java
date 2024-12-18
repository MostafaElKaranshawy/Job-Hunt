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
        signUpRequest.setLastName("ThisIsAVeryLongLastNameThatExceedsTheMaximumLength");

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

    @Test
    public void shouldThrowExceptionWhenLastNameContainsSpace() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setLastName("Mahmoud Ali");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> lastNameValidator.doValidation(signUpRequest));

        assertEquals("Last name must not contain spaces", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenLastNameStartsWithSpace() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setLastName(" Mahmoud");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> lastNameValidator.doValidation(signUpRequest));

        assertEquals("Last name must not contain spaces", exception.getMessage());
    }


    @Test
    public void shouldThrowExceptionWhenLastNameContainsNumber() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setLastName("Mahmoud1");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> lastNameValidator.doValidation(signUpRequest));

        assertEquals("Last name must not contain numbers", exception.getMessage());
    }
    @Test
    public void shouldThrowExceptionWhenLastNameContainsSpecialCharacters() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setLastName("Youssef@");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> lastNameValidator.doValidation(signUpRequest));

        assertEquals("Last name must not contain special characters", exception.getMessage());
    }

}