package com.software.backend.service;

import com.software.backend.auth.AuthenticationResponse;
import com.software.backend.config.JwtService;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.Company;
import com.software.backend.entity.Token;
import com.software.backend.entity.User;
import com.software.backend.enums.TokenType;
import com.software.backend.enums.UserType;
import com.software.backend.enums.ValidationType;
import com.software.backend.exception.BusinessException;
import com.software.backend.repository.CompanyRepository;
import com.software.backend.repository.TokenRepository;
import com.software.backend.repository.UserRepository;
import com.software.backend.validation.validators.Validator;
import com.software.backend.validation.ValidationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyAuthService {

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    private final JwtService jwtService;

    public AuthenticationResponse signUp(SignUpRequest signUpRequest) {
        Validator validator = ValidationFactory.createValidator(ValidationType.COMPANY_SIGNUP);
        validator.validate(signUpRequest);  // to be checked later to prevent it from being null(refactor)


        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent())
            throw new BusinessException("Email already exists.");

        String username = signUpRequest.getEmail().split("@")[0];
        var user = User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .userType(UserType.COMPANY)
                .username(username)
                .isBanned(false)
                .build();

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        Company company = new Company();
        company.setUser(user);
        company.setName(signUpRequest.getCompanyName());


        // Save both User and Company entities
        userRepository.save(user);
        companyRepository.save(company);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
