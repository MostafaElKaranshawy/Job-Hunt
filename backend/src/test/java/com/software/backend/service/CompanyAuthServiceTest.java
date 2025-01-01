//package com.software.backend.service;
//
//import com.software.backend.dto.SignUpRequest;
//import com.software.backend.entity.User;
//import com.software.backend.enums.UserType;
//import com.software.backend.exception.EmailAlreadyRegisteredException;
//import com.software.backend.repository.UserRepository;
//import com.software.backend.util.JwtUtil;
//import com.software.backend.validation.ValidationFactory;
//import com.software.backend.validation.validators.Validator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//class CompanyAuthServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private EmailService emailService;
//
//    @Mock
//    private JwtUtil jwtUtil;
//
//    @Mock
//    private PasswordService passwordService;
//
//    @Mock
//    private Validator validator;
//
//    @InjectMocks
//    private CompanyAuthService companyAuthService;
//
//    private SignUpRequest signUpRequest;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        signUpRequest = new SignUpRequest();
//        signUpRequest.setCompanyName("Test Company");
//        signUpRequest.setEmail("test@gmail.com");
//        signUpRequest.setPassword("Password123@");
//
//        // Mocking static method for ValidationFactory.createValidator using Mockito-inline
//        // To do this correctly, ensure Mockito-inline is in your dependencies if static mocking is required
//        // mockStatic(ValidationFactory.class);
//        // when(ValidationFactory.createValidator(ValidationType.COMPANY_SIGNUP)).thenReturn(validator);
//    }
//
//    @Test
//    void testSignUp_Success() {
//        // Arrange
//        when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.empty());
//        when(passwordService.hashPassword(signUpRequest.getPassword())).thenReturn("hashedPassword");
//        when(jwtUtil.generateSignupToken(signUpRequest)).thenReturn("signupToken");
//        when(ValidationFactory.createValidator(any())).thenReturn(validator);
//
//        // Act
//        companyAuthService.signUp(signUpRequest);
//
//        // Assert
//        verify(validator).validate(signUpRequest); // Validate request
//        verify(userRepository).findByEmail(signUpRequest.getEmail()); // Check email existence
//        verify(passwordService).hashPassword(signUpRequest.getPassword()); // Hash password
//        verify(jwtUtil).generateSignupToken(signUpRequest); // Generate signup token
//        verify(emailService).sendConfirmationEmail(signUpRequest.getEmail(), "signupToken"); // Send confirmation email
//    }
//
//    @Test
//    void testSignUp_EmailAlreadyRegistered() {
//        // Arrange: Mock an existing user in the database
//        when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.of(new User()));
//
//        // Act & Assert
//        EmailAlreadyRegisteredException exception = assertThrows(
//                EmailAlreadyRegisteredException.class,
//                () -> companyAuthService.signUp(signUpRequest)
//        );
//        assertEquals("Email already exists.", exception.getMessage());
//        verify(validator).validate(signUpRequest); // Ensure validation still occurs
//        verify(userRepository).findByEmail(signUpRequest.getEmail()); // Check email existence
//        verifyNoInteractions(passwordService, jwtUtil, emailService); // Ensure no further actions are performed
//    }
//
//    @Test
//    void testSignUp_ValidationFailure() {
//        // Arrange: Mock validation failure
//        doThrow(new IllegalArgumentException("Invalid input data"))
//                .when(validator).validate(signUpRequest);
//
//        // Act & Assert
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> companyAuthService.signUp(signUpRequest)
//        );
//        assertEquals("Invalid input data", exception.getMessage());
//        verify(validator).validate(signUpRequest); // Ensure validation is invoked
//        verifyNoInteractions(userRepository, passwordService, jwtUtil, emailService); // Ensure no further actions
//    }
//}
