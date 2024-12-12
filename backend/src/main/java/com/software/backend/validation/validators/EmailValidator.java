package com.software.backend.validation.validators;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;

import java.util.regex.Pattern;

public class EmailValidator extends Validator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@gmail\\.com$");


    @Override
    public void doValidation(SignUpRequest signUpRequest){
        String email = signUpRequest.getEmail();

        if (email == null || email.isEmpty())
            throw new BusinessException("Email is required");

        if (!EMAIL_PATTERN.matcher(email).matches())
            throw new BusinessException("Email must be a valid email address");

    }
}
