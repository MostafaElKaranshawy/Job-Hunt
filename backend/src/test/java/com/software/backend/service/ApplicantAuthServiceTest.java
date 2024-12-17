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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.env.Environment;

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
    private Environment env;

    @Mock
    private PasswordService passwordService;

    @Mock
    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the Validator behavior
        doNothing().when(validator).validate(any(SignUpRequest.class));

        // Mock other dependencies
        when(passwordService.hashPassword(anyString())).thenReturn("hashedPassword123");
        when(jwtUtil.generateSignupToken(any(SignUpRequest.class))).thenReturn("signupToken123");
    }

    @Test
    void testApplicantGoogleSignUp_WhenEmailAlreadyExists_ThrowsException() {
        // Arrange
        SignUpRequest request = new SignUpRequest();
        request.setGoogleToken("validToken123");

        JsonWebSignature mockToken = mock(JsonWebSignature.class);
        JsonWebToken.Payload payload = new JsonWebToken.Payload();
        payload.set("email", "test@example.com");
        payload.set("name", "John Doe");

        when(tokenService.verifyGoogleToken("validToken123")).thenReturn(mockToken);
        when(mockToken.getPayload()).thenReturn(payload);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(EmailAlreadyRegisteredException.class, () -> applicantAuthService.applicantGoogleSignUp(request));
    }

    @Test
    void testApplicantGoogleSignUp_Success() {
        // Arrange
        SignUpRequest request = new SignUpRequest();
        request.setGoogleToken("validToken123");

        JsonWebSignature mockToken = mock(JsonWebSignature.class);
        JsonWebToken.Payload payload = new JsonWebToken.Payload();
        payload.set("email", "test@example.com");
        payload.set("name", "John Doe");

        when(tokenService.verifyGoogleToken("validToken123")).thenReturn(mockToken);
        when(mockToken.getPayload()).thenReturn(payload);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act
        applicantAuthService.applicantGoogleSignUp(request);

        // Assert
        verify(userRepository).save(any(User.class));
        verify(applicantRepository).save(any(Applicant.class));
    }

    @Test
    void testLoginWithGoogle_WhenUserNotFound_ThrowsException() {
        // Arrange
        SignUpRequest request = new SignUpRequest();
        request.setGoogleToken("validToken123");

        JsonWebSignature mockToken = mock(JsonWebSignature.class);
        JsonWebToken.Payload payload = new JsonWebToken.Payload();
        payload.set("email", "test@example.com");

        when(tokenService.verifyGoogleToken("validToken123")).thenReturn(mockToken);
        when(mockToken.getPayload()).thenReturn(payload);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> applicantAuthService.loginWithGoogle(request));
    }

    @Test
    void testLoginWithGoogle_WhenNotGoogleRegistered_ThrowsException() {
        // Arrange
        SignUpRequest request = new SignUpRequest();
        request.setGoogleToken("validToken123");

        User user = User.builder().email("test@example.com").password("password123").build();
        JsonWebSignature mockToken = mock(JsonWebSignature.class);
        JsonWebToken.Payload payload = new JsonWebToken.Payload();
        payload.set("email", "test@example.com");

        when(tokenService.verifyGoogleToken("validToken123")).thenReturn(mockToken);
        when(mockToken.getPayload()).thenReturn(payload);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> applicantAuthService.loginWithGoogle(request));
    }

    @Test
    void testLoginWithGoogle_Success() {
        // Arrange
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

        // Act
        AuthenticationResponse response = applicantAuthService.loginWithGoogle(request);

        // Assert
        assertNotNull(response);
        assertEquals("accessToken123", response.getAccessToken());
        assertEquals("refreshToken123", response.getRefreshToken());
        assertEquals("johndoe", response.getUsername());
    }

    @Test
    void testLoginWithGoogle_WhenTokenIsInvalid_ThrowsException() {
        // Arrange
        SignUpRequest request = new SignUpRequest();
        request.setGoogleToken("invalidToken");

        when(tokenService.verifyGoogleToken("invalidToken")).thenThrow(new RuntimeException("Invalid token"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            applicantAuthService.loginWithGoogle(request);
        });

        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void testLoginWithGoogle_WhenUserFoundAndSuccessful_ReturnsAuthenticationResponse() {
        // Arrange
        SignUpRequest request = new SignUpRequest();
        request.setGoogleToken("validToken");

        User user = User.builder()
                .email("test@example.com")
                .username("johndoe")
                .password(null) // Indicates Google signup
                .build();

        JsonWebSignature mockToken = mock(JsonWebSignature.class);
        JsonWebToken.Payload payload = new JsonWebToken.Payload();
        payload.set("email", "test@example.com");

        when(tokenService.verifyGoogleToken("validToken")).thenReturn(mockToken);
        when(mockToken.getPayload()).thenReturn(payload);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateAccessToken("johndoe")).thenReturn("accessToken123");
        when(jwtUtil.generateRefreshToken("johndoe")).thenReturn("refreshToken123");

        // Act
        AuthenticationResponse response = applicantAuthService.loginWithGoogle(request);

        // Assert
        assertNotNull(response);
        assertEquals("accessToken123", response.getAccessToken());
        assertEquals("refreshToken123", response.getRefreshToken());
        assertEquals("johndoe", response.getUsername());
    }
}
