package com.software.backend.service;

import com.software.backend.auth.AuthenticationResponse;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.InvalidCredentialsException;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.repository.UserRepository;
import com.software.backend.util.CookieUtil;
import com.software.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationResponse login(SignUpRequest request) {

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        String username = user.getUsername();
        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);
        System.out.println("login successful");

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(username)
                .build();
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

        refreshTokenService.saveNewRefreshTokenInDb(authenticationResponse.getRefreshToken(), authenticationResponse.getUsername());

    }
}
