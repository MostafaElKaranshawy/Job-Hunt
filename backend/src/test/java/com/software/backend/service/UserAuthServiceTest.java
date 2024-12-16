package com.software.backend.service;

import com.software.backend.dto.AuthenticationResponse;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.User;
import com.software.backend.exception.InvalidCredentialsException;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.repository.UserRepository;
import com.software.backend.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAuthServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserAuthService userAuthService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void login_Success() {
//        // Arrange
//        String email = "test@example.com";
//        String password = "password123";
//        String encodedPassword = "encodedPassword123";
//        String username = "testuser";
//        String accessToken = "accessToken";
//        String refreshToken = "refreshToken";
//
//        SignUpRequest request = new SignUpRequest(email, password);
//        User user = new User();
//        user.setEmail(email);
//        user.setPassword(encodedPassword);
//        user.setUsername(username);
//
//        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
//        when(jwtUtil.generateAccessToken(username)).thenReturn(accessToken);
//        when(jwtUtil.generateRefreshToken(username)).thenReturn(refreshToken);
//
//        // Act
//        AuthenticationResponse response = userAuthService.login(request);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(accessToken, response.getAccessToken());
//        assertEquals(refreshToken, response.getRefreshToken());
//        assertEquals(username, response.getUsername());
//
//        verify(repository, times(1)).findByEmail(email);
//        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
//        verify(jwtUtil, times(1)).generateAccessToken(username);
//        verify(jwtUtil, times(1)).generateRefreshToken(username);
//    }
//
//    @Test
//    void login_UserNotFound() {
//        // Arrange
//        String email = "test@example.com";
//        String password = "password123";
//        SignUpRequest request = new SignUpRequest(email, password);
//
//        when(repository.findByEmail(email)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userAuthService.login(request));
//        assertEquals("User not found", exception.getMessage());
//
//        verify(repository, times(1)).findByEmail(email);
//        verifyNoInteractions(passwordEncoder);
//        verifyNoInteractions(jwtUtil);
//    }
//
//    @Test
//    void login_InvalidCredentials() {
//        // Arrange
//        String email = "test@example.com";
//        String password = "wrongPassword";
//        String encodedPassword = "encodedPassword123";
//        String username = "testuser";
//
//        SignUpRequest request = new SignUpRequest(email, password);
//        User user = new User();
//        user.setEmail(email);
//        user.setPassword(encodedPassword);
//        user.setUsername(username);
//
//        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);
//
//        // Act & Assert
//        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> userAuthService.login(request));
//        assertEquals("Invalid credentials", exception.getMessage());
//
//        verify(repository, times(1)).findByEmail(email);
//        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
//        verifyNoInteractions(jwtUtil);
//    }
}
