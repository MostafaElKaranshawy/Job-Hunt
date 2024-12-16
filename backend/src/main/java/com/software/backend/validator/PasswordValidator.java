package com.software.backend.validator;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;

public class PasswordValidator extends Validator {

    @Override
    public void validate(SignUpRequest signUpRequest){ //throws ValidationException {
        String password = signUpRequest.getPassword();
//        System.out.println("Password: " + password);
        if (password == null || password.isEmpty()) {
            throw new BusinessException("Password is required.");
        } else if (password.length() <= 8) {
            throw new BusinessException("Password must be at least 8 characters long.");
        } else if (!password.matches(".*[A-Z].*")) {
            throw new BusinessException("Password must contain at least one uppercase letter.");
        } else if (!password.matches(".*[a-z].*")) {
            throw new BusinessException("Password must contain at least one lowercase letter.");
        } else if (!password.matches(".*[0-9].*")) {
            throw new BusinessException("Password must contain at least one number.");
        } else if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new BusinessException("Password must contain at least one special character.");
        }


        if (nextValidator != null) {
            nextValidator.validate(signUpRequest);
        }
    }
}
