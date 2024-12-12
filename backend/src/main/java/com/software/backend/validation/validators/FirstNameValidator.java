package com.software.backend.validation.validators;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;

public class FirstNameValidator extends Validator {

    @Override
    public void doValidation(SignUpRequest signUpRequest){
        String firstName = signUpRequest.getFirstName();

        if (firstName == null || firstName.isEmpty())
            throw new BusinessException("First name is required");

        if (firstName.length() >= 30)
            throw new BusinessException("First name must not exceed 30 characters");

    }
}
