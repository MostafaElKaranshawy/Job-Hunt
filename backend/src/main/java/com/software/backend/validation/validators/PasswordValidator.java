package com.software.backend.validation.validators;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;

public class PasswordValidator extends Validator {

    @Override
    public void doValidation(SignUpRequest signUpRequest){
        String password = signUpRequest.getPassword();

        if (password == null || password.isEmpty())
            throw new BusinessException("Password is required.");

        if (password.length() <= 8)
            throw new BusinessException("Password must be at least 8 characters long.");

        if (!password.matches(".*[A-Z].*"))
            throw new BusinessException("Password must contain at least one uppercase letter.");

        if (!password.matches(".*[a-z].*"))
            throw new BusinessException("Password must contain at least one lowercase letter.");

        if (!password.matches(".*[0-9].*"))
            throw new BusinessException("Password must contain at least one number.");

        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*"))
            throw new BusinessException("Password must contain at least one special character.");

    }
}
