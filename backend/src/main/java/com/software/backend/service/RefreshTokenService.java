package com.software.backend.service;


import com.software.backend.entity.RefreshToken;
import com.software.backend.repository.RefreshTokenRepository;
import com.software.backend.util.CookieUtil;
import com.software.backend.util.JwtUtil;
import com.software.backend.util.SpringContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Component
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000; // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 days
    private final RefreshTokenRepository refreshTokenRepository;

    public boolean validateRefreshToken(String refreshToken) {
        // Fetch the refresh token from the database
        String storedRefreshToken = fetchRefreshTokenFromDb(refreshToken);
        if (storedRefreshToken == null) {
            return false;
        }
        return JwtUtil.validateToken(storedRefreshToken);
    }

    public void createNewTokens(String refreshToken,HttpServletResponse response) {
        // Fetch the username from the refresh token
        String username = JwtUtil.getUsernameFromRefreshToken(refreshToken);
        // Generate new access and refresh tokens
        String newAccessToken = JwtUtil.generateAccessToken(username);
        String newRefreshToken = JwtUtil.generateRefreshToken(username);
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

        // Set the expiration time, e.g., 1 min from now
        LocalDateTime expiresAt = now.plusMinutes(1);
        RefreshToken refreshToken = new RefreshToken(token, username, now, expiresAt);
        System.out.println("Saving refresh token in the database");
        System.out.println("Token: " + token);
        System.out.println("Username: " + username);
        System.out.println("Now: " + now);
        System.out.println("Expires at: " + expiresAt);
        refreshTokenRepository.save(refreshToken);

    }

}
