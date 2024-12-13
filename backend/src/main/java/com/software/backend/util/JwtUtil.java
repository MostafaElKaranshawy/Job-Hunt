package com.software.backend.util;

import com.software.backend.dto.SignUpRequest;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
@Component
public class JwtUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long ACCESS_TOKEN_EXPIRATION = 60 * 1000; // 1 minute
    private static final long REFRESH_TOKEN_EXPIRATION = 5* 60 * 1000; // 1 days

    public static String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public static String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public static Boolean validateToken(String token) {
        try {
            // Parse the token and extract claims
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)  // Replace 'key' with your signing key
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Check if the token is expired
            if (claims.getExpiration().before(new Date())) {
                return false; // Token is expired
            }

            // If everything is valid, return true
            return true;

        } catch (Exception e) {
            // If any exception occurs (malformed token, expired, etc.), return false
            return false;
        }
    }


    public static String getUsernameFromRefreshToken(String refreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody()
                .getSubject();
    }

    public String generateSignupToken(SignUpRequest request) {
        return Jwts.builder()
                .claim("email", request.getEmail())
                .claim("password", request.getPassword())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1-hour expiration
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }
}

//    public SignUpRequest validateSignupToken(String token) {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(key)
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            // Extract user data from the token
//            String email = claims.get("email", String.class);
//            String password = claims.get("password", String.class);
//            String username = claims.get("username", String.class);
//
//            return new SignUpRequest(email, password);
//        } catch (JwtException | IllegalArgumentException e) {
//            throw new IllegalArgumentException("Invalid or expired token");
//        }
//}
