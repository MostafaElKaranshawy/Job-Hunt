package com.software.backend.service;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.software.backend.dto.AuthenticationResponse;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.Applicant;
import com.software.backend.entity.User;
import com.software.backend.enums.UserType;
import com.software.backend.enums.ValidationType;
import com.software.backend.exception.EmailAlreadyRegisteredException;
import com.software.backend.exception.InvalidCredentialsException;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.UserRepository;
import com.software.backend.util.JwtUtil;
import com.software.backend.validation.ValidationFactory;
import com.software.backend.validation.validators.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class ApplicantAuthService {

    private final UserRepository userRepository;

    private final ApplicantRepository applicantRepository;

    private final EmailService emailService;

    private final TokenService tokenService;

    private final JwtUtil jwtUtil;

    private final Environment env;

    private final PasswordService passwordService;


    public void signUp(SignUpRequest signUpRequest) {
        System.out.println("Applicant Sign-Up from service");
        Validator validator = ValidationFactory.createValidator(ValidationType.APPLICANT_SIGNUP);
        validator.validate(signUpRequest);
        System.out.println("Validated sign-up request data");
        signUpRequest.setPassword(passwordService.hashPassword(signUpRequest.getPassword()));
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent())
            throw new EmailAlreadyRegisteredException("Email already exists.");
        signUpRequest.setUserType(UserType.APPLICANT);
        String signUpToken = jwtUtil.generateSignupToken(signUpRequest);
        emailService.sendConfirmationEmail(signUpRequest.getEmail(), signUpToken);
        System.out.println("Email sent");
    }

    public void applicantGoogleSignUp(SignUpRequest signUpRequest) {

        JsonWebSignature verifiedToken = tokenService.verifyGoogleToken(signUpRequest.getGoogleToken());
        JsonWebToken.Payload payload = verifiedToken.getPayload();
        String email = payload.get("email").toString();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyRegisteredException("Email already exists.");
        }

        String username = email.split("@")[0];
        var user = User.builder()
                .email(email)
                .userType(UserType.APPLICANT)
                .username(username)
                .isBanned(false)
                .build();
        String name = payload.get("name").toString();
        // Create the Applicant entity
        Applicant applicant = new Applicant();
        applicant.setUser(user);
        applicant.setFirstName(name.split(" ")[0]);
        applicant.setLastName(name.split(" ")[1]);

        // Save both User and Applicant entities
        userRepository.save(user);
        applicantRepository.save(applicant);
    }

    public AuthenticationResponse loginWithGoogle(SignUpRequest request) {
        JsonWebSignature verifiedToken = tokenService.verifyGoogleToken(request.getGoogleToken());

        JsonWebToken.Payload payload = verifiedToken.getPayload();

        String email = payload.get("email").toString();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        System.out.println("User found");
        if ( user.getPassword() != null ) {
            throw new InvalidCredentialsException("User is not registered with Google");
        }

        String username = user.getUsername();
        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(username)
                .build();
    }

}
