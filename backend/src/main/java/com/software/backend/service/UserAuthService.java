package com.software.backend.service;

import com.software.backend.dto.AuthenticationResponse;
import com.software.backend.dto.LogInRequest;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.Applicant;
import com.software.backend.entity.Company;
import com.software.backend.entity.User;
import com.software.backend.enums.UserType;
import com.software.backend.exception.EmailAlreadyRegisteredException;
import com.software.backend.exception.InvalidCredentialsException;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.CompanyRepository;
import com.software.backend.repository.UserRepository;
import com.software.backend.util.JwtUtil;
import com.software.backend.validation.validators.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final ApplicantRepository applicantRepository;
    private final CompanyRepository companyRepository;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final PasswordService passwordService;

    public AuthenticationResponse login(LogInRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("email or password is incorrect"));


        System.out.println("Username found");
        if (!passwordService.verifyPassword(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("email or password is incorrect");
        }
        String username = user.getUsername();
        String userType = String.valueOf(user.getUserType());
        System.out.println(userType);
        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(username)
                .userType(userType)
                .build();
    }

    public void createNewUser(SignUpRequest request) {
        String username = request.getEmail().split("@")[0];

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new EmailAlreadyRegisteredException("User already exists");
        }
//        request.setPassword(passwordService.hashPassword(request.getPassword()));
        var user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .userType(request.getUserType())
                .username(username)
                .isBanned(false)
                .build();
        userRepository.save(user);
        if (request.getUserType().equals(UserType.APPLICANT)) {
            Applicant applicant = new Applicant();
            applicant.setUser(user);
            applicant.setFirstName(request.getFirstName());
            applicant.setLastName(request.getLastName());
            applicantRepository.save(applicant);
            System.out.println("Applicant saved");
            } else if (request.getUserType().equals(UserType.COMPANY)) {
            Company company = new Company();
            company.setUser(user);
            company.setName(request.getCompanyName());
            companyRepository.save(company);
            System.out.println("Company saved");
        }

    }

    public void resetPasswordRequest(String email){
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        System.out.println("User found");
        if(user.getPassword() == null){
            throw new InvalidCredentialsException("User is google authenticated");
        }
        String resetPasswordToken = jwtUtil.generateResetPasswordToken(email);
        System.out.println("Reset password token generated");
        emailService.sendResetEmail(email, resetPasswordToken);
    }

    public void resetPassword(String resetToken, String password){
        String email = jwtUtil.validateResetPasswordToken(resetToken);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        SignUpRequest SignUpRequest = new SignUpRequest();
        SignUpRequest.setPassword(password);
        PasswordValidator passwordValidator = new PasswordValidator();
        passwordValidator.validate(SignUpRequest);
        password = passwordService.hashPassword(password);
        user.setPassword(password);
        userRepository.save(user);
        System.out.println("Password reset");
    }
}
