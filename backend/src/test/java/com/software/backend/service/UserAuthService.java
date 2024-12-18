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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicantRepository applicantRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private UserAuthService userAuthService;

    private SignUpRequest signUpRequest;
    private LogInRequest logInRequest;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample sign up request
        signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("testuser@example.com");
        signUpRequest.setPassword("password123");
        signUpRequest.setUserType(UserType.APPLICANT);
        signUpRequest.setFirstName("John");
        signUpRequest.setLastName("Doe");

        // Sample login request
        logInRequest = new LogInRequest();
        logInRequest.setEmail("testuser@example.com");
        logInRequest.setPassword("password123");

        // Sample user object
        user = new User();
        user.setEmail("testuser@example.com");
        user.setUsername("testuser");
        user.setPassword("hashedpassword123");
    }

    @Test
    void testLogin_validCredentials_returnsAuthenticationResponse() {
        when(userRepository.findByEmail(logInRequest.getEmail())).thenReturn(java.util.Optional.of(user));
        when(passwordService.verifyPassword(logInRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateAccessToken(user.getUsername())).thenReturn("newAccessToken");
        when(jwtUtil.generateRefreshToken(user.getUsername())).thenReturn("newRefreshToken");

        AuthenticationResponse response = userAuthService.login(logInRequest);

        assertNotNull(response);
        assertEquals("newAccessToken", response.getAccessToken());
        assertEquals("newRefreshToken", response.getRefreshToken());
        assertEquals("testuser", response.getUsername());
    }

    @Test
    void testLogin_invalidCredentials_throwsInvalidCredentialsException() {
        when(userRepository.findByEmail(logInRequest.getEmail())).thenReturn(java.util.Optional.of(user));
        when(passwordService.verifyPassword(logInRequest.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userAuthService.login(logInRequest));
    }

    @Test
    void testCreateNewUser_userAlreadyExists_throwsEmailAlreadyRegisteredException() {
        when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(java.util.Optional.of(user));

        assertThrows(EmailAlreadyRegisteredException.class, () -> userAuthService.createNewUser(signUpRequest));
    }

    @Test
    void testCreateNewUser_success_createsNewUser() {
        when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(java.util.Optional.empty());
        when(passwordService.hashPassword(signUpRequest.getPassword())).thenReturn("hashedPassword123");

        userAuthService.createNewUser(signUpRequest);

        verify(userRepository).save(any(User.class));
        if (signUpRequest.getUserType() == UserType.APPLICANT) {
            verify(applicantRepository).save(any(Applicant.class));
        } else {
            verify(companyRepository).save(any(Company.class));
        }
    }

    @Test
    void testResetPasswordRequest_userNotFound_throwsUserNotFoundException() {
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userAuthService.resetPasswordRequest("notfound@example.com"));
    }

    @Test
    void testResetPasswordRequest_googleAuthenticatedUser_throwsInvalidCredentialsException() {
        user.setPassword(null);  // Google Authenticated user does not have a password
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.of(user));

        assertThrows(InvalidCredentialsException.class, () -> userAuthService.resetPasswordRequest("testuser@example.com"));
    }

    @Test
    void testResetPassword_resetPasswordTokenGenerated_sendsEmail() {
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.of(user));
        when(jwtUtil.generateResetPasswordToken(any())).thenReturn("resetToken");

        userAuthService.resetPasswordRequest("testuser@example.com");

        verify(emailService).sendResetEmail(any(), eq("resetToken"));
    }

    @Test
    void testResetPassword_invalidResetToken_throwsUserNotFoundException() {
        when(jwtUtil.validateResetPasswordToken(any())).thenThrow(InvalidCredentialsException.class);

        assertThrows(InvalidCredentialsException.class, () -> userAuthService.resetPassword("invalidToken", "newPassword"));
    }

    @Test
    void testResetPassword_success_resetsPassword() {
        when(jwtUtil.validateResetPasswordToken(any())).thenReturn("testuser@example.com");
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.of(user));
        when(passwordService.hashPassword("newPassword")).thenReturn("hashedNewPassword");

        userAuthService.resetPassword("validToken", "newPassword1@");

        verify(userRepository).save(any(User.class));
    }
}
