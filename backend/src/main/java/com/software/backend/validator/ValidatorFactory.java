package com.software.backend.validator;

import com.software.backend.enums.ValidationType;
import com.software.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidatorFactory {

    @Autowired
    private UserRepository userRepository;


    public static Validator createValidator(ValidationType type) {
        System.out.println("Creating validator chain for " + type);
        if (type == ValidationType.APPLICANT_SIGNUP) {
            return createApplicantSignUpValidatorChain();
        }
        else if (type == ValidationType.COMPANY_SIGNUP) {
            return createCompanySignUpValidatorChain();
        }
        else {
            return null;
        }
    }

    private static Validator createCompanySignUpValidatorChain() {
        Validator companyNameValidator = new CompanyNameValidator();
        Validator emailValidator = new EmailValidator();
        Validator passwordValidator = new PasswordValidator();

        companyNameValidator.setNext(emailValidator);
        emailValidator.setNext(passwordValidator);

        return companyNameValidator;
    }

    private static Validator createApplicantSignUpValidatorChain() {
        Validator firstNameValidator = new FirstNameValidator();
        Validator lastNameValidator = new LastNameValidator();
        Validator emailValidator = new EmailValidator();
        Validator passwordValidator = new PasswordValidator();

        firstNameValidator.setNext(lastNameValidator);
        lastNameValidator.setNext(emailValidator);
        emailValidator.setNext(passwordValidator);

        return firstNameValidator;
    }
}
