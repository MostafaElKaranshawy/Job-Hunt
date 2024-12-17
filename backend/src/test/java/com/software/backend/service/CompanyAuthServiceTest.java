package com.software.backend.service;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.enums.UserType;
import com.software.backend.enums.ValidationType;
import com.software.backend.exception.EmailAlreadyRegisteredException;
import com.software.backend.repository.UserRepository;
import com.software.backend.util.JwtUtil;
import com.software.backend.validation.ValidationFactory;
import com.software.backend.validation.validators.Validator;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyAuthServiceTest {

    @InjectMocks
    private CompanyAuthService companyAuthService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordService passwordService;

    @Mock
    private Validator validator;

    private static MockedStatic<ValidationFactory> validationFactoryMock;

    @BeforeAll
    static void initStaticMock() {
        validationFactoryMock = Mockito.mockStatic(ValidationFactory.class);
    }

    @AfterAll
    static void closeStaticMock() {
        validationFactoryMock.close();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock static method ValidationFactory.createValidator
        validationFactoryMock.when(() -> ValidationFactory.createValidator(ValidationType.COMPANY_SIGNUP))
                .thenReturn(validator);

        // Mock validator behavior
        doNothing().when(validator).validate(any(SignUpRequest.class));

        // Other mocks
        when(passwordService.hashPassword(anyString())).thenReturn("hashedPassword123");
        when(jwtUtil.generateSignupToken(any(SignUpRequest.class))).thenReturn("signupToken123");
    }

    @Test
    void testSignUp_Success() {
        // Arrange
        SignUpRequest request = new SignUpRequest();
        request.setEmail("company@example.com");
        request.setPassword("securePassword");

        when(userRepository.findByEmail("company@example.com")).thenReturn(Optional.empty());

        // Act
        companyAuthService.signUp(request);

        // Assert
        verify(validator).validate(request);
        verify(userRepository).findByEmail("company@example.com");
        verify(passwordService).hashPassword("securePassword");
        verify(jwtUtil).generateSignupToken(request);
        verify(emailService).sendConfirmationEmail("company@example.com", "signupToken123");

        assertEquals(UserType.COMPANY, request.getUserType());
    }

    @Test
    void testSignUp_WhenEmailAlreadyRegistered_ThrowsException() {
        // Arrange
        SignUpRequest request = new SignUpRequest();
        request.setEmail("company@example.com");

        when(userRepository.findByEmail("company@example.com")).thenReturn(Optional.of(new com.software.backend.entity.User()));

        // Act & Assert
        EmailAlreadyRegisteredException exception = assertThrows(EmailAlreadyRegisteredException.class, () -> {
            companyAuthService.signUp(request);
        });

        assertEquals("Email already exists.", exception.getMessage());
        verify(userRepository).findByEmail("company@example.com");
        verifyNoInteractions(passwordService, jwtUtil, emailService);
    }
}
