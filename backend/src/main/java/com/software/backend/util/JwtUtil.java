package com.software.backend.util;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.enums.UserType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
@Component
public class JwtUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long ACCESS_TOKEN_EXPIRATION = 60 * 1000; // 1 minute
    private static final long REFRESH_TOKEN_EXPIRATION = 5 * 60 * 1000; // 1 days

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
        System.out.println("userType: " + request.getUserType());
        return Jwts.builder()

                .claim("userType", request.getUserType())
                .claim("email", request.getEmail())
                .claim("password", request.getPassword())
                .claim("firstName", request.getFirstName())
                .claim("lastName", request.getLastName())
                .claim("companyName", request.getCompanyName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))// 15 m expiration
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public SignUpRequest validateSignupToken(String token) {
        System.out.println("inside validateSignupToken");
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
            SignUpRequest request = new SignUpRequest();
            request.setUserType(UserType.valueOf(claims.get("userType", String.class)));
            request.setEmail(claims.get("email", String.class));
            request.setPassword(claims.get("password", String.class));
            request.setFirstName(claims.get("firstName", String.class));
            request.setLastName(claims.get("lastName", String.class));
            request.setCompanyName(claims.get("companyName", String.class));
            System.out.println("token validated");
            return request;
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid or expired token");
        }
    }
}
