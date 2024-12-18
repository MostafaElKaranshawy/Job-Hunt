package com.software.backend.validation.validators;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;


public class LastNameValidator extends Validator {

    @Override
    public void doValidation(SignUpRequest signUpRequest){
        String lastName = signUpRequest.getLastName();

        if (lastName == null || lastName.isEmpty())
            throw new BusinessException("Last name is required");
        if(lastName.contains(" "))
            throw new BusinessException("Last name must not contain spaces");
        if(lastName.matches(".*\\d.*"))
            throw new BusinessException("Last name must not contain numbers");
        if (lastName.length() >= 30)
            throw new BusinessException("Last name must not exceed 30 characters");

    }
}
