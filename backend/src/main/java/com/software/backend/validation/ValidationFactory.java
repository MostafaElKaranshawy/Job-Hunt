package com.software.backend.validation;

import com.software.backend.enums.ValidationType;
import com.software.backend.validation.chains.ApplicantSignUpValidationChain;
import com.software.backend.validation.chains.CompanySignUpValidationChain;
import com.software.backend.validation.validators.*;

public class ValidationFactory {

    public static Validator createValidator(ValidationType type) {
        System.out.println("Creating validator chain for " + type);
        if (type == ValidationType.APPLICANT_SIGNUP)
            return new ApplicantSignUpValidationChain().getChain();

        if (type == ValidationType.COMPANY_SIGNUP)
            return new CompanySignUpValidationChain().getChain();

        return null;
    }

}
