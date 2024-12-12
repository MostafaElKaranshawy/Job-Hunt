package com.software.backend.validation.validators;

import com.software.backend.dto.SignUpRequest;

public abstract class Validator {
    protected Validator nextValidator;

    // Set the next validator in the chain
    public void setNext(Validator nextValidator) {
        this.nextValidator = nextValidator;
    }

    public Validator getNext() { return nextValidator; }

    // Abstract validation method
    protected abstract void doValidation(SignUpRequest signUpRequest); //throws ValidationException;


    public void validate(SignUpRequest signUpRequest) {
        doValidation(signUpRequest);

        if (nextValidator != null) nextValidator.validate(signUpRequest);
    }

}