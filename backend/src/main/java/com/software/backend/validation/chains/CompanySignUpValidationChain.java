package com.software.backend.validation.chains;

import com.software.backend.validation.validators.*;

public class CompanySignUpValidationChain extends ValidationChain {


    @Override
    public Validator getChain() {
        CompanyNameValidator companyNameValidator = new CompanyNameValidator();
        EmailValidator emailValidator = new EmailValidator();
        EmailUniquenessValidator emailUniquenessValidator = new EmailUniquenessValidator();
        PasswordValidator passwordValidator = new PasswordValidator();

        companyNameValidator.setNext(emailValidator);
        emailValidator.setNext(emailUniquenessValidator);
        emailUniquenessValidator.setNext(passwordValidator);

        return companyNameValidator;
    }
}
