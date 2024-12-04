package com.software.backend.service;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.Applicant;
import com.software.backend.entity.User;
import com.software.backend.enums.UserType;
import com.software.backend.enums.ValidationType;
import com.software.backend.exception.BusinessException;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.UserRepository;
import com.software.backend.validator.Validator;
import com.software.backend.validator.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicantAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    public void signUp(SignUpRequest signUpRequest) {
        // Check if the email is already registered
//        System.out.println(signUpRequest.toString());
//        User user = userRepository.findByEmail(signUpRequest.getEmail());
//        if (user != null)
//            throw new BusinessException("Email already registered.");
        try {
            Validator validator = ValidatorFactory.createValidator(ValidationType.APPLICANT_SIGNUP);
            validator.validate(signUpRequest);
            if (userRepository.findByEmail(signUpRequest.getEmail()) != null) {
                throw new BusinessException("Email already exists.");
            }

        }
        catch (BusinessException e) {
            System.out.println(e);
            throw new BusinessException(e.getMessage());
        }


        // Create the User entity
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getEmail().split("@")[0]); // Generate username
        user.setPassword(signUpRequest.getPassword());
        user.setUserType(UserType.APPLICANT);
        user.setIsBanned(false);

        // Create the Applicant entity
        Applicant applicant = new Applicant();
        applicant.setUser(user);
        applicant.setFirstName(signUpRequest.getFirstName());
        applicant.setLastName(signUpRequest.getLastName());


        // Save both User and Applicant entities
        userRepository.save(user);
        applicantRepository.save(applicant);

    }



}
