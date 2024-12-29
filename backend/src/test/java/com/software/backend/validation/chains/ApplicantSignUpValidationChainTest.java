package com.software.backend.validation.chains;

import com.software.backend.validation.validators.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicantSignUpValidationChainTest {

    @Test
    public void testValidationChainSetup() {
        // Create an instance of the validation chain
        ApplicantSignUpValidationChain validationChain = new ApplicantSignUpValidationChain();

        // Retrieve the chain's head
        Validator chainHead = validationChain.getChain();  // First Name

        // Verify the chain structure
        assertNotNull(chainHead, "The chain head should not be null");
        assertInstanceOf(FirstNameValidator.class, chainHead, "The first validator should be FirstNameValidator");

        Validator second = chainHead.getNext();

        assertNotNull(second, "The second validator should not be null");
        assertInstanceOf(LastNameValidator.class, second, "The second validator should be LastNameValidator");

        Validator third = second.getNext();

        assertNotNull(third, "The third validator should not be null");
        assertInstanceOf(EmailValidator.class, third, "The third validator should be EmailValidator");

        Validator fourth = third.getNext();

        assertNotNull(fourth, "The fourth validator should not be null");
        assertInstanceOf(EmailUniquenessValidator.class, fourth, "The fourth validator should be EmailUniquenessValidator");

        Validator fifth = fourth.getNext();

        assertNotNull(fifth, "The fifth validator should not be null");
        assertInstanceOf(PasswordValidator.class, fifth, "The fifth validator should be PasswordValidator");

        // Ensure the chain ends after the fifth validator
        assertNull(fifth.getNext(), "The last validator should not point to another validator");
    }
}
