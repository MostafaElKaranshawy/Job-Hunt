package com.software.backend.service;

import com.software.backend.dto.ChangePasswordDto;
import com.software.backend.entity.User;
import com.software.backend.exception.InvalidCredentialsException;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServicesTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private UserServices userServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testGetUser_UserExists() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        User result = userServices.getUser("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void testGetUser_UserNotExists() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        User result = userServices.getUser("testUser");

        assertNull(result);
    }

    @Test
    void testChangePassword_Success() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setUserName("testUser");
        dto.setOldPassword("oldPass@");
        dto.setNewPassword("newPass123@");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("hashedOldPass@");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordService.hashPassword("oldPass@")).thenReturn("hashedOldPass@");
        when(passwordService.hashPassword("newPass123@")).thenReturn("hashedNewPass123@");

        userServices.changePassword(dto);

        verify(userRepository).save(user);
        assertEquals("hashedNewPass123@", user.getPassword());
    }

    @Test
    void testChangePassword_UserNotFound() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setUserName("unknownUser");
        dto.setOldPassword("oldPass@");
        dto.setNewPassword("newPass123@");

        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServices.changePassword(dto));
    }

    @Test
    void testChangePassword_InvalidOldPassword() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setUserName("testUser");
        dto.setOldPassword("wrongPass@");
        dto.setNewPassword("newPass123@");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("hashedOldPass@");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordService.hashPassword("wrongPass@")).thenReturn("hashedWrongPass@");

        assertThrows(InvalidCredentialsException.class, () -> userServices.changePassword(dto));
    }
}
