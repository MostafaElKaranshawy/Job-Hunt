package com.software.backend.validator;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;

public class FirstNameValidator extends Validator {

    @Override
    public void validate(SignUpRequest signUpRequest){ // throws ValidationException {
        String firstName = signUpRequest.getFirstName();

        if (firstName == null || firstName.isEmpty()) {
            throw new BusinessException("First name is required.");
        } else if (firstName.length() >= 30) {
            throw new BusinessException("First name must not exceed 30 characters.");
        }

        if (nextValidator != null) {
            nextValidator.validate(signUpRequest);
        }
    }
}
