package com.software.backend.service;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.auth.oauth2.TokenVerifier;
import com.software.backend.dto.AuthenticationResponse;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.Applicant;
import com.software.backend.entity.User;
import com.software.backend.enums.UserType;
import com.software.backend.enums.ValidationType;
import com.software.backend.exception.BusinessException;
import com.software.backend.exception.EmailAlreadyRegisteredException;
import com.software.backend.exception.InvalidCredentialsException;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.UserRepository;
import com.software.backend.util.JwtUtil;
import com.software.backend.validator.Validator;
import com.software.backend.validator.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class ApplicantAuthService {

    private final UserRepository userRepository;

    private final ApplicantRepository applicantRepository;

    private final EmailService emailService;

    private final JwtUtil jwtUtil;

    private final Environment env;

    public void signUp(SignUpRequest signUpRequest) {
        System.out.println("Applicant Sign-Up from service");
        Validator validator = ValidatorFactory.createValidator(ValidationType.APPLICANT_SIGNUP);
        validator.validate(signUpRequest);
        System.out.println("Validated sign-up request data");
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent())
            throw new EmailAlreadyRegisteredException("Email already exists.");
        signUpRequest.setUserType(UserType.APPLICANT);
        String signUpToken = jwtUtil.generateSignupToken(signUpRequest);
        emailService.sendConfirmationEmail(signUpRequest.getEmail(), signUpToken);
        System.out.println("Email sent");
    }

    private JsonWebSignature verifyGoogleToken(String idToken) {
        if (idToken == null || idToken.isEmpty()) {
            throw new InvalidCredentialsException("Google Token is required");
        }
        idToken = idToken.replace("\"", "");
        try {
            TokenVerifier tokenVerifier = TokenVerifier.newBuilder()
                    .setAudience(env.getProperty("GOOGLE_CLIENT_ID"))
                    .build();
            JsonWebSignature verifiedToken = tokenVerifier.verify(idToken);
            return verifiedToken;
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid Google Token");
        }
    }

    public AuthenticationResponse applicantGoogleSignUp(SignUpRequest signUpRequest) {
        JsonWebSignature verifiedToken = verifyGoogleToken(signUpRequest.getGoogleToken());

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
        return AuthenticationResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }

    public AuthenticationResponse loginWithGoogle(SignUpRequest request) {
        JsonWebSignature verifiedToken = verifyGoogleToken(request.getGoogleToken());

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
