package com.software.backend.service;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.auth.oauth2.TokenVerifier;
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

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Captor
    private ArgumentCaptor<RefreshToken> refreshTokenCaptor;

    private final Clock fixedClock = Clock.fixed(LocalDateTime.now().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

    private String refreshToken;
    private String username;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tokenService = new TokenService(env, refreshTokenRepository, jwtUtil);
        refreshToken = "sampleRefreshToken";
        username = "testUser";
    }

    @Test
    void testValidateRefreshToken_invalidToken_returnsFalse() {
        when(refreshTokenRepository.findByToken(refreshToken)).thenReturn(null);

        boolean result = tokenService.validateRefreshToken(refreshToken);

        assertFalse(result);
        verify(refreshTokenRepository).findByToken(refreshToken);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void testCreateNewTokens_createsNewTokensAndAddsToResponse() {
        // Arrange
        String newAccessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";
        RefreshToken mockRefreshToken = new RefreshToken(); // Create a mock RefreshToken object

        when(jwtUtil.getUsernameFromRefreshToken(refreshToken)).thenReturn(username);
        when(jwtUtil.generateAccessToken(username)).thenReturn(newAccessToken);
        when(jwtUtil.generateRefreshToken(username)).thenReturn(newRefreshToken);
        when(refreshTokenRepository.findByToken(refreshToken)).thenReturn(mockRefreshToken);

        // Act
        tokenService.createNewTokens(refreshToken, response);

        // Assert
        verify(jwtUtil).getUsernameFromRefreshToken(refreshToken);
        verify(jwtUtil).generateAccessToken(username);
        verify(jwtUtil).generateRefreshToken(username);
        verify(refreshTokenRepository).save(mockRefreshToken); // Verify that save was called with the correct object
        verify(response, times(2)).addCookie(any());
    }


    @Test
    void testSaveNewRefreshTokenInDb_savesTokenInDb() {
        // Arrange
        String newRefreshToken = "newRefreshToken";
        RefreshToken mockRefreshToken = new RefreshToken();
        ArgumentCaptor<RefreshToken> refreshTokenCaptor = ArgumentCaptor.forClass(RefreshToken.class); // Declare captor
        LocalDateTime now = LocalDateTime.now(); // Simulate current time
        long expirationTime = 3600000; // 1 hour in milliseconds
        when(env.getProperty("REFRESH_TOKEN_EXPIRATION")).thenReturn(String.valueOf(expirationTime));

        // Act
        tokenService.saveNewRefreshTokenInDb(newRefreshToken, username);

        // Assert
        verify(refreshTokenRepository).save(refreshTokenCaptor.capture()); // Capture saved token
        RefreshToken savedToken = refreshTokenCaptor.getValue();

        assertEquals(newRefreshToken, savedToken.getToken());
        assertEquals(username, savedToken.getUsername());
        assertEquals(now.plus(expirationTime, ChronoUnit.MILLIS).truncatedTo(ChronoUnit.SECONDS),
                savedToken.getExpiresAt().truncatedTo(ChronoUnit.SECONDS)); // Ignore precision differences
    }

    @Test
    void testVerifyGoogleToken_invalidToken_throwsException() {
        String invalidIdToken = "invalidGoogleIdToken";

        assertThrows(InvalidCredentialsException.class, () -> {
            tokenService.verifyGoogleToken(invalidIdToken);
        });
    }
}
