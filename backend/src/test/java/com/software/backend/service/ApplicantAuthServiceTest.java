package com.software.backend.service;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.software.backend.dto.AuthenticationResponse;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.Applicant;
import com.software.backend.entity.User;
import com.software.backend.enums.UserType;
import com.software.backend.exception.EmailAlreadyRegisteredException;
import com.software.backend.exception.InvalidCredentialsException;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.UserRepository;
import com.software.backend.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicantAuthServiceTest {

    @InjectMocks
    private ApplicantAuthService applicantAuthService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicantRepository applicantRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private TokenService tokenService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApplicantGoogleSignUp_WhenEmailAlreadyExists_ThrowsException() {
        SignUpRequest request = new SignUpRequest();
        request.setGoogleToken("validToken123");

        JsonWebSignature mockToken = mock(JsonWebSignature.class);
        JsonWebToken.Payload payload = new JsonWebToken.Payload();
        payload.set("email", "test@example.com");
        payload.set("name", "John Doe");

        when(tokenService.verifyGoogleToken("validToken123")).thenReturn(mockToken);
        when(mockToken.getPayload()).thenReturn(payload);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyRegisteredException.class, () -> applicantAuthService.applicantGoogleSignUp(request));
    }

    @Test
    void testApplicantGoogleSignUp_Success() {
        SignUpRequest request = new SignUpRequest();
        request.setGoogleToken("validToken123");

        JsonWebSignature mockToken = mock(JsonWebSignature.class);
        JsonWebToken.Payload payload = new JsonWebToken.Payload();
        payload.set("email", "test@example.com");
        payload.set("name", "John Doe");

        when(tokenService.verifyGoogleToken("validToken123")).thenReturn(mockToken);
        when(mockToken.getPayload()).thenReturn(payload);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        applicantAuthService.applicantGoogleSignUp(request);

        verify(userRepository).save(any(User.class));
        verify(applicantRepository).save(any(Applicant.class));
    }

    @Test
    void testLoginWithGoogle_WhenUserNotFound_ThrowsException() {
        SignUpRequest request = new SignUpRequest();
        request.setGoogleToken("validToken123");

        JsonWebSignature mockToken = mock(JsonWebSignature.class);
        JsonWebToken.Payload payload = new JsonWebToken.Payload();
        payload.set("email", "test@example.com");

        when(tokenService.verifyGoogleToken("validToken123")).thenReturn(mockToken);
        when(mockToken.getPayload()).thenReturn(payload);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> applicantAuthService.loginWithGoogle(request));
    }

    @Test
    void testLoginWithGoogle_WhenNotGoogleRegistered_ThrowsException() {
        SignUpRequest request = new SignUpRequest();
        request.setGoogleToken("validToken123");

        User user = User.builder().email("test@example.com").password("password123").build();
        JsonWebSignature mockToken = mock(JsonWebSignature.class);
        JsonWebToken.Payload payload = new JsonWebToken.Payload();
        payload.set("email", "test@example.com");

        when(tokenService.verifyGoogleToken("validToken123")).thenReturn(mockToken);
        when(mockToken.getPayload()).thenReturn(payload);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class, () -> applicantAuthService.loginWithGoogle(request));
    }

    @Test
    void testLoginWithGoogle_Success() {
        SignUpRequest request = new SignUpRequest();
        request.setGoogleToken("validToken123");

        User user = User.builder().email("test@example.com").username("johndoe").password(null).build();
        JsonWebSignature mockToken = mock(JsonWebSignature.class);
        JsonWebToken.Payload payload = new JsonWebToken.Payload();
        payload.set("email", "test@example.com");

        when(tokenService.verifyGoogleToken("validToken123")).thenReturn(mockToken);
        when(mockToken.getPayload()).thenReturn(payload);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateAccessToken("johndoe")).thenReturn("accessToken123");
        when(jwtUtil.generateRefreshToken("johndoe")).thenReturn("refreshToken123");

        AuthenticationResponse response = applicantAuthService.loginWithGoogle(request);

        assertNotNull(response);
        assertEquals("accessToken123", response.getAccessToken());
        assertEquals("refreshToken123", response.getRefreshToken());
        assertEquals("johndoe", response.getUsername());
    }

    @Test
    void testLoginWithGoogle_WhenTokenIsInvalid_ThrowsException() {
        SignUpRequest request = new SignUpRequest();
        request.setGoogleToken("invalidToken");

        when(tokenService.verifyGoogleToken("invalidToken")).thenThrow(new RuntimeException("Invalid token"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            applicantAuthService.loginWithGoogle(request);
        });

        assertEquals("Invalid token", exception.getMessage());
    }
}
