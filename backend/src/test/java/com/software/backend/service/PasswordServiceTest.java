package com.software.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceTest {

    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        passwordService = new PasswordService();
    }

    @Test
    void testHashPassword_Success() {
        String password = "testPassword123";
        String hashedPassword = passwordService.hashPassword(password);

        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);  // Ensure the hashed password is not the same as the raw password
    }

    @Test
    void testHashPassword_DifferentPasswords() {
        String password1 = "password1";
        String password2 = "password2";

        String hashedPassword1 = passwordService.hashPassword(password1);
        String hashedPassword2 = passwordService.hashPassword(password2);

        assertNotEquals(hashedPassword1, hashedPassword2);  // Different passwords should result in different hashes
    }

    @Test
    void testVerifyPassword_Success() {
        String password = "testPassword123";
        String hashedPassword = passwordService.hashPassword(password);

        assertTrue(passwordService.verifyPassword(password, hashedPassword));  // Verify the password matches
    }

    @Test
    void testVerifyPassword_Failure() {
        String password = "testPassword123";
        String incorrectPassword = "wrongPassword";
        String hashedPassword = passwordService.hashPassword(password);

        assertFalse(passwordService.verifyPassword(incorrectPassword, hashedPassword));  // Wrong password should not match
    }
}
