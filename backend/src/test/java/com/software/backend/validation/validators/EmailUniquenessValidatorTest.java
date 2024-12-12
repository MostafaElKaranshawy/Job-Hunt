package com.software.backend.validation.validators;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.entity.User;
import com.software.backend.exception.BusinessException;
import com.software.backend.repository.UserRepository;
import com.software.backend.util.SpringContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailUniquenessValidatorTest {

    private EmailUniquenessValidator emailUniquenessValidator;

    private static UserRepository userRepositoryMock;

    private static MockedStatic<SpringContext> springContextMock;

    @BeforeAll
    static void setUpAll() {
        userRepositoryMock = mock(UserRepository.class);

        // Mock SpringContext to return the mocked UserRepository
        springContextMock = mockStatic(SpringContext.class);
        springContextMock.when(() -> SpringContext.getBean(UserRepository.class))
                .thenReturn(userRepositoryMock);
    }

    @BeforeEach
    void setUp() {
        // Initialize the validator
        emailUniquenessValidator = new EmailUniquenessValidator();

    }

    @Test
    public void shouldThrowExceptionWhenEmailIsNull() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> emailUniquenessValidator.doValidation(signUpRequest));

        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsEmpty() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> emailUniquenessValidator.doValidation(signUpRequest));

        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("youssef@gmail.com");

        // Mock UserRepository behavior to simulate existing user
        when(userRepositoryMock.findByEmail("youssef@gmail.com"))
                .thenReturn(Optional.of(new User()));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> emailUniquenessValidator.doValidation(signUpRequest));

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    public void shouldPassValidationWhenEmailDoesNotExist() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("newuser@gmail.com");

        // Mock UserRepository behavior to simulate no user exists
        when(userRepositoryMock.findByEmail("newuser@gmail.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertDoesNotThrow(() -> emailUniquenessValidator.doValidation(signUpRequest));
    }
}
