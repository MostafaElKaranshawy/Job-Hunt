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
        signUpRequest.setFirstName("ThisIsAVeryLongFirstNameThatExceedsTheMaximumLength");

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

    @Test
    public void shouldThrowExceptionWhenFirstNameContainsSpaces() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("Youssef Mahmoud");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> firstNameValidator.doValidation(signUpRequest));

        assertEquals("First name must not contain spaces", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFirstNameStartsWithSpace() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName(" Youssef");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> firstNameValidator.doValidation(signUpRequest));

        assertEquals("First name must not contain spaces", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFirstNameContainsSpecialCharacters() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("Youssef@");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> firstNameValidator.doValidation(signUpRequest));

        assertEquals("First name must not contain special characters", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFirstNameContainsNumbers() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("Youssef123");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> firstNameValidator.doValidation(signUpRequest));

        assertEquals("First name must not contain numbers", exception.getMessage());
    }


}
