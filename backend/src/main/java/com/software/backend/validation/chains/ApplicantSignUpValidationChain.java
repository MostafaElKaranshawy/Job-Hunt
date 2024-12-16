package com.software.backend.validation.chains;

import com.software.backend.validation.validators.*;


public class ApplicantSignUpValidationChain extends ValidationChain {

    @Override
    public Validator getChain() {

        FirstNameValidator firstNameValidator = new FirstNameValidator();
        LastNameValidator lastNameValidator = new LastNameValidator();
        EmailValidator emailValidator = new EmailValidator();
        EmailUniquenessValidator emailUniquenessValidator = new EmailUniquenessValidator();
        PasswordValidator passwordValidator = new PasswordValidator();

        firstNameValidator.setNext(lastNameValidator);
        lastNameValidator.setNext(emailValidator);
        emailValidator.setNext(emailUniquenessValidator);
        emailUniquenessValidator.setNext(passwordValidator);

        return firstNameValidator;
    }



}
