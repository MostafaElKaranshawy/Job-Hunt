package com.software.backend.util;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.enums.UserType;
import com.software.backend.exception.InvalidCredentialsException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final Environment env;

    private Key secretKey;

    @PostConstruct
    protected void initSecretKey() {
        this.secretKey = new SecretKeySpec(
                Base64.getDecoder().decode(env.getProperty("JWT_SECRET_KEY")),
                SignatureAlgorithm.HS256.getJcaName()
        );
    }

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("ACCESS_TOKEN_EXPIRATION"))))
                .signWith(secretKey)
                .compact();
    }

    public  String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("REFRESH_TOKEN_EXPIRATION")))
                )
                .signWith(secretKey)
                .compact();
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
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000))// 5 m expiration
                .signWith(secretKey)
                .compact();
    }

    public  String generateResetPasswordToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5 minutes
                .signWith(secretKey)
                .compact();
    }

    public  Boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)  // Replace 'key' with your signing key
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (claims.getExpiration().before(new Date()) ) {
                throw new InvalidCredentialsException("Token is expired");
            }
            return true;

        } catch (Exception e) {
            // If any exception occurs (malformed token, expired, etc.), return false
            return false;
        }
    }

    public  String getUsernameFromRefreshToken(String refreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody()
                .getSubject();
    }

    public SignUpRequest validateSignupToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
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
            if (claims.getExpiration().before(new Date())) {
                throw new InvalidCredentialsException("Token has expired");
            }
            return request;
        } catch (JwtException | IllegalArgumentException e ) {
            throw new InvalidCredentialsException("Invalid or expired token");
        }
    }

    public String validateResetPasswordToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            if (expirationDate.before(new Date())) {
                throw new InvalidCredentialsException("Token has expired");
            }
            return claims.getSubject();
        } catch (JwtException e) {
            throw new InvalidCredentialsException("Invalid or expired token");
        } catch (IllegalArgumentException e) {
            throw new InvalidCredentialsException("Token must not be null or empty");
        }
    }

    public Key getSecretKey() {
        return secretKey;
    }
}