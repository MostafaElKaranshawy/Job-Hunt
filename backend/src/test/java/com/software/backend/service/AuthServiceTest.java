package com.software.backend.service;

import com.software.backend.entity.User;
import com.software.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthServiceTest {
    @InjectMocks
    //private AuthService authService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testWiothValidData() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "y@gmil.com";
        String googleClientId = "123";
        String userType = "applicant";
        User mockUser = new User();

        when(userRepository.save(mockUser)).thenReturn(mockUser);
        //User user = authService.createUser(email);
//        assertEquals(user.getEmail(), email);
//        assertEquals(user.getGoogleClientId(), googleClientId);
//        assertEquals(user.getUserType(), userType);
    }
}