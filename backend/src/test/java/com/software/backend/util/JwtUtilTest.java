package com.software.backend.util;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.enums.UserType;
import com.software.backend.exception.InvalidCredentialsException;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import java.security.Key;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @Mock
    private Environment env;

    private Key secretKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256); // Generates a strong secret key
        when(env.getProperty("JWT_SECRET_KEY")).thenReturn(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        when(env.getProperty("ACCESS_TOKEN_EXPIRATION")).thenReturn("3600000"); // 1 hour
        when(env.getProperty("REFRESH_TOKEN_EXPIRATION")).thenReturn("86400000"); // 1 day

        jwtUtil = new JwtUtil(env);
        jwtUtil.initSecretKey(); // Initialize the secret key
    }

    @Test
    void testGenerateAccessToken() {
        String username = "testUser";
        String token = jwtUtil.generateAccessToken(username);

        assertNotNull(token);
        assertDoesNotThrow(() -> jwtUtil.validateToken(token));
    }

    @Test
    void testGenerateRefreshToken() {
        String username = "testUser";
        String token = jwtUtil.generateRefreshToken(username);

        assertNotNull(token);
        assertEquals(username, jwtUtil.getUsernameFromRefreshToken(token));
    }

    @Test
    void testGenerateSignupToken() {
        SignUpRequest request = new SignUpRequest();
        request.setUserType(UserType.APPLICANT);
        request.setEmail("test@gmail.com");
        request.setPassword("Password123!");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setCompanyName("ExampleCorp");

        String token = jwtUtil.generateSignupToken(request);

        assertNotNull(token);
        SignUpRequest validatedRequest = jwtUtil.validateSignupToken(token);

        assertEquals(request.getEmail(), validatedRequest.getEmail());
        assertEquals(request.getUserType(), validatedRequest.getUserType());
    }

    @Test
    void testGenerateResetPasswordToken() {
        String email = "test@gmail.com";
        String token = jwtUtil.generateResetPasswordToken(email);

        assertNotNull(token);
        String validatedEmail = jwtUtil.validateResetPasswordToken(token);

        assertEquals(email, validatedEmail);
    }

    @Test
    void testValidateToken_Valid() {
        String username = "testUser";
        String token = jwtUtil.generateAccessToken(username);

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testValidateToken_Expired() throws InterruptedException {
        when(env.getProperty("ACCESS_TOKEN_EXPIRATION")).thenReturn("1"); // Token expires immediately
        jwtUtil.initSecretKey();
        String token = jwtUtil.generateAccessToken("testUser");

        Thread.sleep(2); // Ensure token is expired
        assertFalse(jwtUtil.validateToken(token));
    }

    @Test
    void testValidateSignupToken_Invalid() {
        String invalidToken = "invalid.token.structure";

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> jwtUtil.validateSignupToken(invalidToken)
        );

        assertEquals("Invalid or expired token", exception.getMessage());
    }

    @Test
    void testValidateResetPasswordToken_Invalid() {
        String invalidToken = "invalid.token.structure";

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> jwtUtil.validateResetPasswordToken(invalidToken)
        );

        assertEquals("Invalid or expired token", exception.getMessage());
    }

    @Test
    void testGetSecretKey() {
        assertNotNull(jwtUtil.getSecretKey());
        assertEquals(secretKey, jwtUtil.getSecretKey());
    }
}
