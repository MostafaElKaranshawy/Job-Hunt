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
    void testHashPassword() {
        String password = "myPassword";
        String hashedPassword = passwordService.hashPassword(password);

        // Ensure the hashed password is not null and not equal to the raw password
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
    }

    @Test
    void testVerifyPassword_Success() {
        String password = "myPassword";
        String hashedPassword = passwordService.hashPassword(password);

        // Verify that the password matches the hashed password
        assertTrue(passwordService.verifyPassword(password, hashedPassword));
    }

    @Test
    void testVerifyPassword_Failure() {
        String password = "myPassword";
        String wrongPassword = "wrongPassword";
        String hashedPassword = passwordService.hashPassword(password);

        // Verify that the password does not match the hashed password
        assertFalse(passwordService.verifyPassword(wrongPassword, hashedPassword));
    }
}
