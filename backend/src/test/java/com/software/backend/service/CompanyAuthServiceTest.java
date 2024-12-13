package com.software.backend.service;

import com.software.backend.auth.AuthenticationResponse;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.Company;
import com.software.backend.entity.User;
import com.software.backend.enums.UserType;
import com.software.backend.enums.ValidationType;
import com.software.backend.exception.BusinessException;
import com.software.backend.repository.CompanyRepository;
import com.software.backend.repository.TokenRepository;
import com.software.backend.repository.UserRepository;
import com.software.backend.validator.Validator;
import com.software.backend.validator.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CompanyAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private ValidatorFactory validatorFactory;

    @Mock
    private Validator validator;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private CompanyAuthService companyAuthService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_WithValidData_ShouldReturnAuthenticationResponse() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@gmail.com");
        signUpRequest.setPassword("Password123#");
        signUpRequest.setCompanyName("Test Company");

        // Mock Validator creation
        try (var mockedValidatorFactory = mockStatic(ValidatorFactory.class)) {
            mockedValidatorFactory
                    .when(() -> ValidatorFactory.createValidator(ValidationType.COMPANY_SIGNUP))
                    .thenReturn(validator);

            doNothing().when(validator).validate(signUpRequest);

            when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());

            User user = User.builder()
                    .email("test@example.com")
                    .username("test")
                    .password("encodedPassword")
                    .userType(UserType.COMPANY)
                    .isBanned(false)
                    .build();

            when(passwordEncoder.encode("Password123#")).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(user);

            when(jwtService.generateToken(user)).thenReturn("mockJwtToken");
            when(jwtService.generateRefreshToken(user)).thenReturn("mockRefreshToken");

            Company company = new Company();
            company.setUser(user);
            company.setName("Test Company");
            when(companyRepository.save(any(Company.class))).thenReturn(company);

            // Act
            AuthenticationResponse response = companyAuthService.signUp(signUpRequest);

            // Assert
            assertNotNull(response);
            assertEquals("mockJwtToken", response.getAccessToken());
            assertEquals("mockRefreshToken", response.getRefreshToken());

            verify(userRepository, times(2)).save(any(User.class));
            verify(companyRepository, times(1)).save(any(Company.class));
            verify(tokenRepository, times(1)).save(any(Token.class));
        }
    }

    @Test
    void signUp_WithExistingEmail_ShouldThrowBusinessException() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@gmail.com");
        signUpRequest.setPassword("Password123#");
        signUpRequest.setCompanyName("Test Company");

        try (var mockedValidatorFactory = mockStatic(ValidatorFactory.class)) {
            mockedValidatorFactory
                    .when(() -> ValidatorFactory.createValidator(ValidationType.COMPANY_SIGNUP))
                    .thenReturn(validator);

            doNothing().when(validator).validate(signUpRequest);

            when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(new User()));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> companyAuthService.signUp(signUpRequest));

            assertEquals("Email already exists.", exception.getMessage());
            verify(userRepository, never()).save(any(User.class));
            verify(companyRepository, never()).save(any(Company.class));
            verify(tokenRepository, never()).save(any(Token.class));
        }
    }

    @Test
    void signUp_WithInvalidValidator_ShouldThrowException() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@gmail.com");
        signUpRequest.setPassword("Password123#");
        signUpRequest.setCompanyName("Test Company");

        try (var mockedValidatorFactory = mockStatic(ValidatorFactory.class)) {
            mockedValidatorFactory
                    .when(() -> ValidatorFactory.createValidator(ValidationType.COMPANY_SIGNUP))
                    .thenThrow(new RuntimeException("Validator creation failed"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> companyAuthService.signUp(signUpRequest));

            assertEquals("Validator creation failed", exception.getMessage());
            verify(userRepository, never()).save(any(User.class));
            verify(companyRepository, never()).save(any(Company.class));
            verify(tokenRepository, never()).save(any(Token.class));
        }
    }
}
