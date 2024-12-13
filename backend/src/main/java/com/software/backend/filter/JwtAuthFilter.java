package com.software.backend.filter;
import com.software.backend.service.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService; // Service for validating refresh tokens
    private final CookieUtil cookieUtil; // Utility for cookie operations

    public JwtAuthFilter(JwtUtil jwtUtil, RefreshTokenService refreshTokenService, CookieUtil cookieUtil) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.cookieUtil = cookieUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        // Skip validation for /auth/** paths
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("In filter");

        // Extract and validate access token
        Optional<Cookie> accessTokenCookie = Optional.ofNullable(cookieUtil.getCookie(request, "accessToken"));
        if (accessTokenCookie.isPresent()) {
            String accessToken = accessTokenCookie.get().getValue();
            System.out.println("Access token cookie: " + accessToken);

            if (jwtUtil.validateToken(accessToken)) {
                // If access token is valid, proceed with the request
                System.out.println("Valid access token");
                try {
                    filterChain.doFilter(request, response);
                    System.out.println("Filter chain processed");
                } catch (Exception e) {
                    System.err.println("Exception occurred while processing filter chain: " + e.getMessage());
                    e.printStackTrace();
                }

                return;
            }
        }

        // If access token is invalid or missing, check refresh token
        Optional<Cookie> refreshTokenCookie = Optional.ofNullable(cookieUtil.getCookie(request, "refreshToken"));
        if (refreshTokenCookie.isPresent()) {
            String refreshToken = refreshTokenCookie.get().getValue();
            System.out.println("Refresh token cookie: " + refreshToken);

            if (refreshTokenService.validateRefreshToken(refreshToken)) {
                // Generate new tokens and set them in the response
                refreshTokenService.createNewTokens(refreshToken, response);
                System.out.println("New tokens generated");
                filterChain.doFilter(request, response);
                return;
            }
        }

        // If both tokens are invalid, reject the request
        System.out.println("Invalid or expired tokens");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid or expired tokens");
    }

}
