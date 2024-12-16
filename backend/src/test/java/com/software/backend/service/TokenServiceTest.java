package com.software.backend.service;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.software.backend.dto.AuthenticationResponse;
import com.software.backend.entity.RefreshToken;
import com.software.backend.exception.InvalidCredentialsException;
import com.software.backend.repository.RefreshTokenRepository;
import com.software.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private Environment env;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private TokenService tokenService;

    private String refreshToken;
    private String username;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        refreshToken = "sampleRefreshToken";
        username = "testUser";
    }

//    @Test
//    void testValidateRefreshToken_validToken_returnsTrue() {
//        // Arrange
//        when(refreshTokenRepository.findByToken(refreshToken)).thenReturn(new RefreshToken());
//        when(jwtUtil.validateToken(refreshToken)).thenReturn(true);
//
//        // Act
//        boolean result = tokenService.validateRefreshToken(refreshToken);
//
//        // Assert
//        assertTrue(result);
//        verify(refreshTokenRepository, times(1)).findByToken(refreshToken);
//        verify(jwtUtil, times(1)).validateToken(refreshToken);
//    }

    @Test
    void testValidateRefreshToken_invalidToken_returnsFalse() {
        // Arrange
        when(refreshTokenRepository.findByToken(refreshToken)).thenReturn(null);

        // Act
        boolean result = tokenService.validateRefreshToken(refreshToken);

        // Assert
        assertFalse(result);
        verify(refreshTokenRepository, times(1)).findByToken(refreshToken);
        verify(jwtUtil, never()).validateToken(refreshToken);
    }

//    @Test
//    void testCreateNewTokens_createsNewTokensAndAddsToResponse() {
//        // Arrange
//        String newAccessToken = "newAccessToken";
//        String newRefreshToken = "newRefreshToken";
//        when(jwtUtil.getUsernameFromRefreshToken(refreshToken)).thenReturn(username);
//        when(jwtUtil.generateAccessToken(username)).thenReturn(newAccessToken);
//        when(jwtUtil.generateRefreshToken(username)).thenReturn(newRefreshToken);
//
//        // Act
//        tokenService.createNewTokens(refreshToken, response);
//
//        // Assert
//        verify(jwtUtil, times(1)).getUsernameFromRefreshToken(refreshToken);
//        verify(jwtUtil, times(1)).generateAccessToken(username);
//        verify(jwtUtil, times(1)).generateRefreshToken(username);
//        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
//        verify(response, times(2)).addCookie(any());
//    }

//    @Test
//    void testSaveNewRefreshTokenInDb_savesTokenInDb() {
//        // Arrange
//        String newRefreshToken = "newRefreshToken";
//        LocalDateTime now = LocalDateTime.now();
//        long expirationTime = 3600000; // 1 hour in milliseconds
//        when(env.getProperty("REFRESH_TOKEN_EXPIRATION")).thenReturn(String.valueOf(expirationTime));
//
//        // Act
//        tokenService.saveNewRefreshTokenInDb(newRefreshToken, username);
//
//        // Assert
//        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
//        verify(refreshTokenRepository, times(1)).save(captor.capture());
//        RefreshToken savedToken = captor.getValue();
//        assertEquals(newRefreshToken, savedToken.getToken());
//        assertEquals(username, savedToken.getUsername());
//        assertEquals(now.plus(expirationTime, ChronoUnit.MILLIS), savedToken.getExpiresAt());
//    }

//    @Test
//    void testVerifyGoogleToken_validToken_returnsVerifiedToken() {
//        // Arrange
//        String validIdToken = "validGoogleIdToken";
//        JsonWebSignature verifiedToken = mock(JsonWebSignature.class);
//        when(env.getProperty("GOOGLE_CLIENT_ID")).thenReturn("your-client-id");
//        when(jwtUtil.verifyGoogleToken(validIdToken)).thenReturn(verifiedToken);
//
//        // Act
//        JsonWebSignature result = tokenService.verifyGoogleToken(validIdToken);
//
//        // Assert
//        assertNotNull(result);
//        verify(jwtUtil, times(1)).verifyGoogleToken(validIdToken);
//    }

    @Test
    void testVerifyGoogleToken_invalidToken_throwsException() {
        String invalidIdToken = "invalidGoogleIdToken";
        when(env.getProperty("GOOGLE_CLIENT_ID")).thenReturn("your-client-id");
        assertThrows(InvalidCredentialsException.class, () -> tokenService.verifyGoogleToken(invalidIdToken));
    }
}
