package com.software.backend.service;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.auth.oauth2.TokenVerifier;
import com.software.backend.auth.AuthenticationResponse;
import com.software.backend.config.JwtService;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.Applicant;
import com.software.backend.entity.Token;
import com.software.backend.entity.User;
import com.software.backend.enums.TokenType;
import com.software.backend.enums.UserType;
import com.software.backend.enums.ValidationType;
import com.software.backend.exception.BusinessException;
import com.software.backend.exception.InvalidCredentialsException;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.TokenRepository;
import com.software.backend.repository.UserRepository;
import com.software.backend.validation.validators.Validator;
import com.software.backend.validation.ValidationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ApplicantAuthService {

    private final UserRepository userRepository;

    private final ApplicantRepository applicantRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final String GOOGLE_CLIENT_ID = "992406545501-gsf4drs652b27m7886oq1ttlfrq3o14t.apps.googleusercontent.com";

    public AuthenticationResponse signUp(SignUpRequest signUpRequest) {
        validateInput(signUpRequest);

        System.out.println("Creating user");
        String username = signUpRequest.getEmail().split("@")[0];
        var user = createUser(signUpRequest, username);

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);


        // Create the Applicant entity
        Applicant applicant = new Applicant();
        applicant.setUser(user);
        applicant.setFirstName(signUpRequest.getFirstName());
        applicant.setLastName(signUpRequest.getLastName());

        // Save both User and Applicant entities
        userRepository.save(user);
        applicantRepository.save(applicant);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void validateInput(SignUpRequest signUpRequest) {
        Validator validator = ValidationFactory.createValidator(ValidationType.APPLICANT_SIGNUP);
        Objects.requireNonNull(validator).validate(signUpRequest);

        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent())
            throw new BusinessException("Email already exists.");
    }

    private User createUser(SignUpRequest signUpRequest, String username) {
        var user = User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .userType(UserType.APPLICANT)
                .username(username)
                .isBanned(false)
                .build();
        return user;
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



    private JsonWebSignature verifyGoogleToken(String idToken) {
        idToken = idToken.replace("\"", "");
        if (idToken.isEmpty()) {
            throw new IllegalArgumentException("idToken is required");
        }

        try {
            TokenVerifier tokenVerifier = TokenVerifier.newBuilder()
                    .setAudience(GOOGLE_CLIENT_ID)
                    .build();
            JsonWebSignature verifiedToken = tokenVerifier.verify(idToken);
            return verifiedToken;

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }


    public AuthenticationResponse applicantGoogleSignUp(SignUpRequest signUpRequest) {
        System.out.println("Google Sign-Up");

        JsonWebSignature verifiedToken = verifyGoogleToken(signUpRequest.getGoogleToken());
        if(verifiedToken == null) throw new BusinessException("Invalid Google Token");
        JsonWebToken.Payload payload = verifiedToken.getPayload();

        String email = payload.get("email").toString();
        String username = email.split("@")[0];
        var user = User.builder()
                .email(email)
                .userType(UserType.APPLICANT)
                .username(username)
                .googleClientId(signUpRequest.getClientId())
                .isBanned(false)
                .build();

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);


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
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse loginWithGoogle(SignUpRequest request) {
        JsonWebSignature verifiedToken = verifyGoogleToken(request.getGoogleToken());
        if(verifiedToken == null) {
            throw new BusinessException("Invalid Google Token");
        }
        JsonWebToken.Payload payload = verifiedToken.getPayload();

        String email = payload.get("email").toString();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if ( user.getPassword() != null ) {
            throw new InvalidCredentialsException("User already exists");
        }

        if ( user.getGoogleClientId() == null ||
             !user.getGoogleClientId().equals(request.getClientId())) {
            throw new InvalidCredentialsException("Invalid Google Client ID");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
