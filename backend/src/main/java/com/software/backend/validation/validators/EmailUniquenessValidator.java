package com.software.backend.validation.validators;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;
import com.software.backend.repository.UserRepository;
import com.software.backend.util.SpringContext;


public class EmailUniquenessValidator extends Validator {

    @Override
    public void doValidation(SignUpRequest signUpRequest){

        String email = signUpRequest.getEmail();

        if (email == null || email.isEmpty())
            throw new BusinessException("Email is required");

        UserRepository userRepository = SpringContext.getBean(UserRepository.class);
        var user = userRepository.findByEmail(email);

        // Check if the email already exists in the database
        boolean emailExists = user.isPresent();
        if (emailExists)
            throw new BusinessException("Email already exists");

    }
}
