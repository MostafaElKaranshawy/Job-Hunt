package com.software.backend.service;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.User;
import com.software.backend.exception.EmailAlreadyRegisteredException;
import com.software.backend.repository.UserRepository;
import com.software.backend.util.JwtUtil;
import com.software.backend.validator.Validator;
import com.software.backend.validator.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.*;

class CompanyAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ValidatorFactory validatorFactory;

    @InjectMocks
    private CompanyAuthService companyAuthService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testSignUp_invalidRequest_throwsException() {
//        SignUpRequest signUpRequest = new SignUpRequest();
//        Validator validator = Mockito.mock(Validator.class);
//        when(validatorFactory.createValidator(any())).thenReturn(validator);
//        doThrow(new RuntimeException()).when(validator).validate(signUpRequest);
//        try {
//            companyAuthService.signUp(signUpRequest);
//        } catch (RuntimeException e) {
//            assert(e.getMessage().equals(null));
//        }
//        verify(validatorFactory, times(1)).createValidator(any());
//        verify(validator, times(1)).validate(signUpRequest);
//    }


    @Test
    void testSignUp_emailAlreadyExists_throwsEmailAlreadyRegisteredException() {

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@gmail.com");
        signUpRequest.setCompanyName("Test Company");
        signUpRequest.setPassword("Password12345@");
        when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.of(new User()));  // Mock email already exists
        try {
            companyAuthService.signUp(signUpRequest);
        } catch (EmailAlreadyRegisteredException e) {

            assert(e.getMessage().equals("Email already exists."));
        }
        verify(userRepository, times(1)).findByEmail(signUpRequest.getEmail());
    }

    @Test
    void testSignUp_validRequest_sendsConfirmationEmail() {

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@gmail.com");
        signUpRequest.setCompanyName("Test Company");
        signUpRequest.setPassword("Password12345@");
        when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.empty());  // Mock email does not exist
        when(jwtUtil.generateSignupToken(signUpRequest)).thenReturn("mockToken");
        companyAuthService.signUp(signUpRequest);
        verify(emailService, times(1)).sendConfirmationEmail(signUpRequest.getEmail(), "mockToken");
    }

}
