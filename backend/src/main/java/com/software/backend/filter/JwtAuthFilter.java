package com.software.backend.filter;

import com.software.backend.service.TokenService;
import com.software.backend.util.CookieUtil;
import com.software.backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil; // Utility for token operations
    private final TokenService refreshTokenService; // Service for validating refresh tokens
    private final CookieUtil cookieUtil; // Utility for cookie operations

    public JwtAuthFilter(JwtUtil jwtUtil, TokenService refreshTokenService, CookieUtil cookieUtil) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.cookieUtil = cookieUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println("Path: " + path);

        // Skip validation for /auth/** paths
        if (path.startsWith("/auth")) {
            System.out.println("Skipping filter for /auth paths");
            filterChain.doFilter(request, response);
            return;
        }
        if (path.equals("/favicon.ico")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Ensure the filter is only processed once per request
            if (request.getAttribute("filterProcessed") != null) {
                filterChain.doFilter(request, response);
                return;
            }

            // Mark the request as processed
            request.setAttribute("filterProcessed", true);

            // Extract and validate access token
            Optional<Cookie> accessTokenCookie = Optional.ofNullable(cookieUtil.getCookie(request, "accessToken"));

            System.out.println("Access token: " + accessTokenCookie);
            if (accessTokenCookie.isPresent() && jwtUtil.validateToken(accessTokenCookie.get().getValue())) {
                System.out.println("Valid access token");
                filterChain.doFilter(request, response);
                return;
            }

            // Check refresh token if access token is invalid or missing
            Optional<Cookie> refreshTokenCookie = Optional.ofNullable(cookieUtil.getCookie(request, "refreshToken"));
            if (refreshTokenCookie.isPresent() && refreshTokenService.validateRefreshToken(refreshTokenCookie.get().getValue())) {
                System.out.println("Valid refresh token. Generating new tokens.");
                refreshTokenService.createNewTokens(refreshTokenCookie.get().getValue(), response);
                filterChain.doFilter(request, response);
                return;
            }

            // If both tokens are invalid, reject the request
            System.out.println("Invalid or expired tokens");
            throw new Exception("Invalid or expired tokens");
        } catch (Exception e) {
            System.err.println("Exception occurred in filter: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("exception occurred in filter: " + e.getMessage());
        }
    }
}
