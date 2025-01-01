package com.software.backend.service;

import com.software.backend.dto.ChangePasswordDto;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.User;
import com.software.backend.exception.InvalidCredentialsException;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.repository.UserRepository;
import com.software.backend.validation.validators.PasswordValidator;
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
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Act
        User result = userServices.getUser("testUser");

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository).findByUsername("testUser");
    }

    @Test
    void testGetUser_UserNotExists() {
        // Arrange
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        // Act
        User result = userServices.getUser("testUser");

        // Assert
        assertNull(result);
        verify(userRepository).findByUsername("testUser");
    }

    @Test
    void testChangePassword_UserNotFound() {
        // Arrange
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setUserName("unknownUser");
        dto.setOldPassword("oldPass@");
        dto.setNewPassword("newPass123@");

        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userServices.changePassword(dto));
        verify(userRepository).findByUsername("unknownUser");
        verifyNoInteractions(passwordService);
    }

    @Test
    void testChangePassword_InvalidOldPassword() {
        // Arrange
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setUserName("testUser");
        dto.setOldPassword("wrongOldPassword");
        dto.setNewPassword("newPassword123@");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("hashedCorrectPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordService.hashPassword("wrongOldPassword")).thenReturn("hashedWrongPassword");

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> userServices.changePassword(dto));
        verify(userRepository).findByUsername("testUser");
        verify(passwordService).hashPassword("wrongOldPassword");
        verifyNoMoreInteractions(passwordService);
    }

    @Test
    void testChangePassword_Success() {
        // Arrange
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setUserName("testUser");
        dto.setOldPassword("correctOldPassword");
        dto.setNewPassword("validNewPassword123@");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("hashedCorrectPassword");

        PasswordValidator passwordValidator = mock(PasswordValidator.class);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordService.hashPassword("correctOldPassword")).thenReturn("hashedCorrectPassword");
        when(passwordService.hashPassword("validNewPassword123@")).thenReturn("hashedNewPassword");

        // Act
        userServices.changePassword(dto);

        // Assert
        verify(userRepository).findByUsername("testUser");
        verify(passwordService).hashPassword("correctOldPassword");
        verify(passwordService).hashPassword("validNewPassword123@");
        verify(userRepository).save(user);
        assertEquals("hashedNewPassword", user.getPassword());
    }

    @Test
    void testChangePassword_InvalidNewPassword() {
        // Arrange
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setUserName("testUser");
        dto.setOldPassword("correctOldPassword");
        dto.setNewPassword("weak");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("hashedCorrectPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordService.hashPassword("correctOldPassword")).thenReturn("hashedCorrectPassword");

        PasswordValidator passwordValidator = mock(PasswordValidator.class);
        doThrow(new IllegalArgumentException("Invalid new password"))
                .when(passwordValidator).validate(any(SignUpRequest.class));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userServices.changePassword(dto));
        verify(userRepository).findByUsername("testUser");
        verify(passwordService).hashPassword("correctOldPassword");
        verifyNoMoreInteractions(passwordService);
    }
}
