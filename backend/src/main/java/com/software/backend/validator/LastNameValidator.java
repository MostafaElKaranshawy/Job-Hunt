package com.software.backend.validator;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;

public class LastNameValidator extends Validator {

    @Override
    public void validate(SignUpRequest signUpRequest){ // throws ValidationException {
        String lastName = signUpRequest.getLastName();

        if (lastName == null || lastName.isEmpty()) {
            throw new BusinessException("Last name is required.");
        } else if (lastName.length() >= 30) {
            throw new BusinessException("Last name must not exceed 30 characters.");
        }

        if (nextValidator != null) {
            nextValidator.validate(signUpRequest);
        }
    }
}
