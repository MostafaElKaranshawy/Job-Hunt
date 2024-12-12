package com.software.backend.validation;

import com.software.backend.enums.ValidationType;
import com.software.backend.validation.chains.ApplicantSignUpValidationChain;
import com.software.backend.validation.chains.CompanySignUpValidationChain;
import com.software.backend.validation.validators.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationFactoryTest {

    @Test
    void shouldReturnSameValidatorOrderAsApplicantSignUpValidationChain() {
        // Arrange
        Validator factoryValidator = ValidationFactory.createValidator(ValidationType.APPLICANT_SIGNUP);
        Validator chainValidator = new ApplicantSignUpValidationChain().getChain();

        // Act & Assert
        while (factoryValidator != null && chainValidator != null) {
            assertEquals(factoryValidator.getClass(), chainValidator.getClass(),
                    "Validators should be in the same order and of the same type");

            factoryValidator = factoryValidator.getNext();
            chainValidator = chainValidator.getNext();
        }

        // Ensure both chains have the same length
        assertNull(factoryValidator, "Factory validator chain should end here");
        assertNull(chainValidator, "Chain validator chain should end here");
    }

    @Test
    void shouldReturnSameValidatorOrderAsCompanySignUpValidationChain() {
        // Arrange
        Validator factoryValidator = ValidationFactory.createValidator(ValidationType.COMPANY_SIGNUP);
        Validator chainValidator = new CompanySignUpValidationChain().getChain();

        // Act & Assert
        while (factoryValidator != null && chainValidator != null) {
            assertEquals(factoryValidator.getClass(), chainValidator.getClass(),
                    "Validators should be in the same order and of the same type");

            factoryValidator = factoryValidator.getNext();
            chainValidator = chainValidator.getNext();
        }

        // Ensure both chains have the same length
        assertNull(factoryValidator, "Factory validator chain should end here");
        assertNull(chainValidator, "Chain validator chain should end here");
    }
}
