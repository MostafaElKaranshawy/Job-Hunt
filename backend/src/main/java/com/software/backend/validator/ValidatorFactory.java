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
        return createApplicantSignUpValidatorChain();
    }

    private static Validator createApplicantSignUpValidatorChain() {
        System.out.println("Creating ApplicantSignUpValidator chain");
        Validator firstNameValidator = new FirstNameValidator();
        Validator lastNameValidator = new LastNameValidator();
        Validator emailValidator = new EmailValidator();
        Validator passwordValidator = new PasswordValidator();

        firstNameValidator.setNext(lastNameValidator);
        lastNameValidator.setNext(emailValidator);
        emailValidator.setNext(passwordValidator);

        System.out.println("ApplicantSignUpValidator chain created");
        return firstNameValidator;
    }
}
