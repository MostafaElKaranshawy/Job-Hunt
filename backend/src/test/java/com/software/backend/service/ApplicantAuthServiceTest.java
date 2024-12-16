package com.software.backend.service;

import com.software.backend.dto.AuthenticationResponse;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.User;
import com.software.backend.exception.EmailAlreadyRegisteredException;
import com.software.backend.exception.InvalidCredentialsException;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.UserRepository;
import com.software.backend.util.JwtUtil;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.env.Environment;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ApplicantAuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private ApplicantRepository applicantRepository;
    @Mock private EmailService emailService;
    @Mock private TokenService tokenService;
    @Mock private JwtUtil jwtUtil;
    @Mock private Environment env;

    @InjectMocks private ApplicantAuthService applicantAuthService;

    private SignUpRequest signUpRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@gmail.com");
        signUpRequest.setGoogleToken("validGoogleToken");
    }

    @Test
    void testSignUp_whenEmailAlreadyExists_shouldThrowEmailAlreadyRegisteredException() {
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(new User()));
        EmailAlreadyRegisteredException exception = assertThrows(EmailAlreadyRegisteredException.class, () -> {
            applicantAuthService.signUp(signUpRequest);
        });

        assertEquals("Email already exists.", exception.getMessage());
    }

    @Test
    void testSignUp_successfulSignUp_shouldSendConfirmationEmail() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());
        when(jwtUtil.generateSignupToken(any(SignUpRequest.class))).thenReturn("signUpToken");

        // When
        applicantAuthService.signUp(signUpRequest);

        // Then
        verify(emailService).sendConfirmationEmail(anyString(), eq("signUpToken"));
    }

    @Test
    void testApplicantGoogleSignUp_whenEmailAlreadyExists_shouldThrowEmailAlreadyRegisteredException() {
        // Given
        when(tokenService.verifyGoogleToken(anyString())).thenReturn(mock(JsonWebSignature.class));
        JsonWebToken.Payload payload = mock(JsonWebToken.Payload.class);
        when(payload.get("email")).thenReturn("existing@applicant.com");
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(new User()));

        // When & Then
        EmailAlreadyRegisteredException exception = assertThrows(EmailAlreadyRegisteredException.class, () -> {
            applicantAuthService.applicantGoogleSignUp(signUpRequest);
        });

        assertEquals("Email already exists.", exception.getMessage());
    }

    @Test
    void testApplicantGoogleSignUp_successfulSignUp_shouldReturnAuthenticationResponse() {
        // Given
        when(tokenService.verifyGoogleToken(anyString())).thenReturn(mock(JsonWebSignature.class));
        JsonWebToken.Payload payload = mock(JsonWebToken.Payload.class);
        when(payload.get("email")).thenReturn("new@applicant.com");
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());
        when(jwtUtil.generateAccessToken(anyString())).thenReturn("accessToken");
        when(jwtUtil.generateRefreshToken(anyString())).thenReturn("refreshToken");

        // When
        AuthenticationResponse response = applicantAuthService.applicantGoogleSignUp(signUpRequest);

        // Then
        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void testLoginWithGoogle_whenUserNotFound_shouldThrowUserNotFoundException() {
        // Given
        when(tokenService.verifyGoogleToken(anyString())).thenReturn(mock(JsonWebSignature.class));
        JsonWebToken.Payload payload = mock(JsonWebToken.Payload.class);
        when(payload.get("email")).thenReturn("nonexistent@applicant.com");
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            applicantAuthService.loginWithGoogle(signUpRequest);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testLoginWithGoogle_whenUserHasPassword_shouldThrowInvalidCredentialsException() {
        // Given
        when(tokenService.verifyGoogleToken(anyString())).thenReturn(mock(JsonWebSignature.class));
        JsonWebToken.Payload payload = mock(JsonWebToken.Payload.class);
        when(payload.get("email")).thenReturn("existing@applicant.com");
        User user = new User();
        user.setEmail("existing@applicant.com");
        user.setPassword("password");
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));

        // When & Then
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            applicantAuthService.loginWithGoogle(signUpRequest);
        });

        assertEquals("User is not registered with Google", exception.getMessage());
    }

    @Test
    void testLoginWithGoogle_successfulLogin_shouldReturnAuthenticationResponse() {
        // Given
        when(tokenService.verifyGoogleToken(anyString())).thenReturn(mock(JsonWebSignature.class));
        JsonWebToken.Payload payload = mock(JsonWebToken.Payload.class);
        when(payload.get("email")).thenReturn("existing@applicant.com");
        User user = new User();
        user.setEmail("existing@applicant.com");
        user.setUsername("existingUser");
        user.setPassword(null); // Google login has no password
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));
        when(jwtUtil.generateAccessToken(anyString())).thenReturn("accessToken");
        when(jwtUtil.generateRefreshToken(anyString())).thenReturn("refreshToken");

        // When
        AuthenticationResponse response = applicantAuthService.loginWithGoogle(signUpRequest);

        // Then
        assertNotNull(response);
        assertEquals("existingUser", response.getUsername());
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }
}
