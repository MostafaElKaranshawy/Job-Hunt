package com.software.backend.config;

import com.software.backend.entity.User;
import com.software.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationConfigTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationConfig applicationConfig;

    private static final String USERNAME = "user@example.com";

    @BeforeEach
    public void setUp() {
        // Initialize mocks and inject them into ApplicationConfig
    }

    @Test
    public void testUserDetailsService() {
        // Arrange: Mock the repository to return a user when queried by username
        when(userRepository.findByUsername(USERNAME)).thenReturn(java.util.Optional.of(new User()));

        // Act: Get the UserDetailsService bean
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();

        // Assert: Verify the user details service works as expected
        assertNotNull(userDetailsService);
        assertDoesNotThrow(() -> userDetailsService.loadUserByUsername(USERNAME));
    }

    @Test
    public void testAuthenticationProvider() {
        // Act: Get the AuthenticationProvider bean
        AuthenticationProvider authProvider = applicationConfig.authenticationProvider();

        // Assert: Verify that the AuthenticationProvider is configured properly
        assertNotNull(authProvider);
        assertTrue(authProvider instanceof DaoAuthenticationProvider);
    }

    @Test
    public void testPasswordEncoder() {
        // Act: Get the PasswordEncoder bean
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();

        // Assert: Verify that the PasswordEncoder is correctly configured
        assertNotNull(passwordEncoder);
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertNotEquals(rawPassword, encodedPassword);
    }

    @Test
    public void testAuthenticationManager() throws Exception {
        // Arrange: Mock AuthenticationConfiguration
        AuthenticationConfiguration config = mock(AuthenticationConfiguration.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(config.getAuthenticationManager()).thenReturn(authenticationManager);

        // Act: Get the AuthenticationManager bean
        AuthenticationManager manager = applicationConfig.authenticationManager(config);

        // Assert: Verify that the AuthenticationManager is properly instantiated
        assertNotNull(manager);
        assertEquals(authenticationManager, manager);
    }
}
