package com.software.backend.service;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.enums.UserType;
import com.software.backend.enums.ValidationType;
import com.software.backend.exception.EmailAlreadyRegisteredException;
import com.software.backend.repository.UserRepository;
import com.software.backend.util.JwtUtil;
import com.software.backend.validation.ValidationFactory;

import com.software.backend.validation.validators.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyAuthService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final JwtUtil jwtUtil;

    private final PasswordService passwordService;

    public void signUp(SignUpRequest signUpRequest) {
        Validator validator = ValidationFactory.createValidator(ValidationType.COMPANY_SIGNUP);
        validator.validate(signUpRequest);  // to be checked later to prevent it from being null(refactor)
        System.out.println("Validated sign-up request data");
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent())
            throw new EmailAlreadyRegisteredException("Email already exists.");
        signUpRequest.setPassword(passwordService.hashPassword(signUpRequest.getPassword()));
        signUpRequest.setUserType(UserType.COMPANY);
        String signUpToken = jwtUtil.generateSignupToken(signUpRequest);
        emailService.sendConfirmationEmail(signUpRequest.getEmail(), signUpToken);
        System.out.println("Email sent");

    }

}
