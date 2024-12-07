package com.software.backend.validator;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;

import java.util.regex.Pattern;

public class EmailValidator extends Validator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@gmail\\.com$");;


    @Override
    public void validate(SignUpRequest signUpRequest){ // throws ValidationException {
        String email = signUpRequest.getEmail();

        if (email == null || email.isEmpty()) {
            throw new BusinessException("Email is required.");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException("Email must be a valid email address.");
        }

        if (nextValidator != null) {
            nextValidator.validate(signUpRequest);
        }
    }
}
