package com.software.backend.validator;

import com.software.backend.dto.SignUpRequest;
import org.springframework.stereotype.Component;

@Component
public abstract class Validator {
    protected Validator nextValidator;

    // Set the next validator in the chain
    public void setNext(Validator nextValidator) {
        this.nextValidator = nextValidator;
    }

    // Abstract validation method
    public abstract void validate(SignUpRequest signUpRequest); //throws ValidationException;
}