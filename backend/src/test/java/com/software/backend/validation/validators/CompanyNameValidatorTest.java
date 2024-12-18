package com.software.backend.validation.validators;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompanyNameValidatorTest {

    private CompanyNameValidator companyNameValidator;

    @BeforeEach
    void setUp() {
        companyNameValidator = new CompanyNameValidator();
    }

    @Test
    public void shouldThrowExceptionWhenCompanyNameIsNull() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setCompanyName(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> companyNameValidator.doValidation(signUpRequest));

        assertEquals("Company name is required", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenCompanyNameIsEmpty() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setCompanyName("");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> companyNameValidator.doValidation(signUpRequest));

        assertEquals("Company name is required", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenCompanyNameIsTooLong() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setCompanyName("This is a very long company name that is more than 30 characters");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> companyNameValidator.doValidation(signUpRequest));

        assertEquals("Company name is too long", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenCompanyNameStartsWithSpace() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setCompanyName(" Company");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> companyNameValidator.doValidation(signUpRequest));

        assertEquals("Company name must not start with a space", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenCompanyNameStartsWithNumber() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setCompanyName("1Company");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> companyNameValidator.doValidation(signUpRequest));

        assertEquals("Company name must not start with a number", exception.getMessage());
    }
}