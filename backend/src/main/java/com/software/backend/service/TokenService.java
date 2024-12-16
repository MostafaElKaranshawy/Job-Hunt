package com.software.backend.service;


import com.software.backend.dto.AuthenticationResponse;
import com.software.backend.entity.RefreshToken;
import com.software.backend.repository.RefreshTokenRepository;
import com.software.backend.util.CookieUtil;
import com.software.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class TokenService {

    private final Environment env;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtUtil jwtUtil;

    public boolean validateRefreshToken(String refreshToken) {
        // Fetch the refresh token from the database
        String storedRefreshToken = fetchRefreshTokenFromDb(refreshToken);
        if (storedRefreshToken == null) {
            return false;
        }
        return jwtUtil.validateToken(storedRefreshToken);
    }

    public void createNewTokens(String refreshToken,HttpServletResponse response) {
        // Fetch the username from the refresh token
        String username = jwtUtil.getUsernameFromRefreshToken(refreshToken);
        // Generate new access and refresh tokens
        String newAccessToken = jwtUtil.generateAccessToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);
        // Save the new refresh token in the database
        saveRefreshTokenInDb(refreshToken, newRefreshToken);
        // Add the new tokens to the response cookies
        CookieUtil.addCookie(response, "accessToken", newAccessToken);
        CookieUtil.addCookie(response, "refreshToken", newRefreshToken);
    }

    private String fetchRefreshTokenFromDb(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
        return refreshToken != null ? refreshToken.getToken() : null;
    }

    public void saveRefreshTokenInDb(String oldToken, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(oldToken);
        refreshToken.setToken(newRefreshToken);
        refreshTokenRepository.save(refreshToken);
    }

    public void saveNewRefreshTokenInDb(String token, String username) {
        LocalDateTime now = LocalDateTime.now();
        long refreshTokenExpiration = Long.parseLong(env.getProperty("REFRESH_TOKEN_EXPIRATION"));
        LocalDateTime expiresAt = now.plus(refreshTokenExpiration, ChronoUnit.MILLIS);
        RefreshToken refreshToken = new RefreshToken(token, username, now, expiresAt);
        System.out.println("Saving refresh token in the database");
        System.out.println("Token: " + token);
        System.out.println("Username: " + username);
        System.out.println("Now: " + now);
        System.out.println("Expires at: " + expiresAt);
        refreshTokenRepository.save(refreshToken);

    }

    public void storeTokens(
            AuthenticationResponse authenticationResponse,
            HttpServletResponse response
    ) {
        authenticationResponse.getAccessToken();
        authenticationResponse.getRefreshToken();
        CookieUtil cookieUtil = new CookieUtil();
        cookieUtil.addCookie(response, "accessToken", authenticationResponse.getAccessToken());
        cookieUtil.addCookie(response, "refreshToken", authenticationResponse.getRefreshToken());
       saveNewRefreshTokenInDb(authenticationResponse.getRefreshToken(), authenticationResponse.getUsername());
        System.out.println("tokens stored");
    }


}
