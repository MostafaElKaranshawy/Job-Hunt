package com.software.backend.service;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.Company;
import com.software.backend.entity.User;
import com.software.backend.enums.UserType;
import com.software.backend.exception.BusinessException;
import com.software.backend.repository.CompanyRepository;
import com.software.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyAuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public void signUp(SignUpRequest signUpRequest) {
        // Check if the email is already registered
        System.out.println(signUpRequest.toString());
        User user = userRepository.findByEmail(signUpRequest.getEmail());
        if (user != null)
            throw new BusinessException("Email already registered.");



        // Create the User entity
        user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getEmail().split("@")[0]); // Generate username
        user.setPassword(signUpRequest.getPassword());
        user.setUserType(UserType.COMPANY);
        user.setIsBanned(false);

        // Create the Company entity
        Company company = new Company();
        company.setUser(user);
        company.setName(signUpRequest.getCompanyName());

        // Save both User and Company entities
        userRepository.save(user);
        companyRepository.save(company);
    }
}
