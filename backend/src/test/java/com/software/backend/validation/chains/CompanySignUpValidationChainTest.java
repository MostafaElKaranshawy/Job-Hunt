package com.software.backend.validation.chains;

import com.software.backend.validation.validators.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CompanySignUpValidationChainTest {

    @Test
    public void testValidationChainSetup() {
        // Create an instance of the validation chain
        CompanySignUpValidationChain validationChain = new CompanySignUpValidationChain();

        // Retrieve the chain's head
        Validator chainHead = validationChain.getChain(); // Company Name

        // Verify the chain structure
        assertNotNull(chainHead, "The chain head should not be null");
        assertInstanceOf(CompanyNameValidator.class, chainHead,
                "The first validator should be CompanyNameValidator");

        Validator second = chainHead.getNext();

        assertNotNull(second, "The second validator should not be null");
        assertInstanceOf(EmailValidator.class, second, "The second validator should be EmailValidator");

        Validator third = second.getNext();

        assertNotNull(third, "The third validator should not be null");
        assertInstanceOf(EmailUniquenessValidator.class, third,
                "The third validator should be EmailUniquenessValidator");

        Validator fourth = third.getNext();

        assertNotNull(fourth, "The fourth validator should not be null");
        assertInstanceOf(PasswordValidator.class, fourth, "The fourth validator should be PasswordValidator");

        // Ensure the chain ends after the fourth validator
        assertNull(fourth.getNext(), "The last validator should not point to another validator");
    }
}
