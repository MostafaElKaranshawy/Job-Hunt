package com.software.backend.validation.validators;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;

public class CompanyNameValidator extends Validator {

    @Override
    public void doValidation(SignUpRequest signUpRequest) {
        String name = signUpRequest.getCompanyName();

        if (name == null || name.isEmpty())
            throw new BusinessException("Company name is required.");

    }

}
