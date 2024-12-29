package com.software.backend.service;

import com.software.backend.service.PasswordService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class PasswordServiceTest {

    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final PasswordService passwordService = new PasswordService();

//    @Test
//    public void testHashPassword() {
//        String rawPassword = "mySecretPassword";
//        String hashedPassword = "hashedPassword";
//
//        when(passwordEncoder.encode(rawPassword)).thenReturn(hashedPassword);
//
//        assertEquals(hashedPassword, passwordService.hashPassword(rawPassword));
//        verify(passwordEncoder, times(1)).encode(rawPassword);
//    }

//    @Test
//    public void testVerifyPassword() {
//        String rawPassword = "mySecretPassword";
//        String hashedPassword = "hashedPassword";
//
//        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);
//        when(passwordEncoder.matches("wrongPassword", hashedPassword)).thenReturn(false);
//
//        assertTrue(passwordService.verifyPassword(rawPassword, hashedPassword));
//        assertFalse(passwordService.verifyPassword("wrongPassword", hashedPassword));
//
//        verify(passwordEncoder, times(2)).matches(anyString(), eq(hashedPassword));
//    }
}
