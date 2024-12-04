package com.software.backend.service;

import com.software.backend.dto.ApplicantDTO;
import com.software.backend.entity.Applicant;
import com.software.backend.entity.User;
import com.software.backend.mapper.ApplicantMapper;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicantServicesTest {

    @InjectMocks
    private ApplicantServices service;

    @Mock
    private ApplicantRepository applicantRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private ApplicantMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getApplicant_ValidUsername_ReturnsApplicantDTO() {
        String username = "testUser";
        User user = new User();
        user.setId(1);
        Applicant applicant = new Applicant();
        ApplicantDTO applicantDTO = new ApplicantDTO();

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(applicantRepo.findById(user.getId())).thenReturn(Optional.of(applicant));
        when(mapper.applicantToApplicantDTO(applicant)).thenReturn(applicantDTO);

        ApplicantDTO result = service.getApplicant(username);

        assertNotNull(result);
        assertEquals(applicantDTO, result);
        verify(userRepo,times(1)).findByUsername(username);
        verify(applicantRepo,times(1)).findById(user.getId());
        verify(mapper,times(1)).applicantToApplicantDTO(applicant);
    }

    @Test
    void getApplicant_InvalidUsername_ReturnsNull() {
        String username = "invalidUser";

        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        ApplicantDTO result = service.getApplicant(username);

        assertNull(result);
        verify(userRepo,times(1)).findByUsername(username);
        verify(applicantRepo, never()).findById(anyInt());
        verify(mapper, never()).applicantToApplicantDTO(any());
    }

    @Test
    void updateApplicant_ValidData_ReturnsTrue() {
        String username = "testUser";
        ApplicantDTO applicantDTO = new ApplicantDTO();
        User user = new User();
        user.setId(1);
        Applicant applicant = new Applicant();

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(applicantRepo.findById(user.getId())).thenReturn(Optional.of(applicant));
        doNothing().when(mapper).updateApplicantFromDTO(applicantDTO, applicant);

        boolean result = service.updateApplicant(username, applicantDTO);

        assertTrue(result);
        verify(userRepo,times(1)).findByUsername(username);
        verify(applicantRepo,times(1)).findById(user.getId());
        verify(mapper,times(1)).updateApplicantFromDTO(applicantDTO, applicant);
        verify(applicantRepo,times(1)).save(applicant);
    }

    @Test
    void updateApplicant_InvalidUsername_ReturnsFalse() {
        String username = "invalidUser";
        ApplicantDTO applicantDTO = new ApplicantDTO();

        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = service.updateApplicant(username, applicantDTO);

        assertFalse(result);
        verify(userRepo,times(1)).findByUsername(username);
        verify(applicantRepo, never()).findById(anyInt());
        verify(mapper, never()).updateApplicantFromDTO(any(), any());
        verify(applicantRepo, never()).save(any());
    }

    @Test
    void getSkills_ValidUsername_ReturnsSkills() {
        String username = "testUser";
        int userId = 1;

        // Mock User object
        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        // Mock Applicant object
        Applicant applicant = new Applicant();
        List<String> skills = List.of("Java", "Spring");
        applicant.setSkills(skills);

        // Mock repository calls
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(applicantRepo.findById(userId)).thenReturn(Optional.of(applicant)); // Use `repo` if that's the actual reference

        // Call the service method
        List<String> result = service.getSkills(username);

        // Assertions
        assertNotNull(result, "Result should not be null");
        assertEquals(skills, result, "Skills should match the mocked skills");

        // Verify interactions
        verify(userRepo,times(1)).findByUsername(username);
        verify(applicantRepo,times(1)).findById(userId); // Use `repo` if that's the actual reference
    }

    @Test
    void getSkills_UserNotFound_ThrowsException() {
        String username = "nonExistentUser";

        // Mock repository call
        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        // Assert exception
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.getSkills(username));

        assertEquals("User not found with username: " + username, exception.getMessage());

        // Verify interaction
        verify(userRepo,times(1)).findByUsername(username);
        verifyNoInteractions(applicantRepo);
    }

    @Test
    void getSkills_ApplicantNotFound_ReturnsNull() {
        String username = "testUser";
        int userId = 1;

        // Mock User object
        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        // Mock repository calls
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(applicantRepo.findById(userId)).thenReturn(Optional.empty());

        // Call the service method
        List<String> result = service.getSkills(username);

        // Assertions
        assertNull(result);

        // Verify interactions
        verify(userRepo,times(1)).findByUsername(username);
        verify(applicantRepo,times(1)).findById(userId);
    }


    @Test
    void setSkills_ValidUsername_SetsSkillsAndReturnsTrue() {
        String username = "testUser";
        User user = new User();
        user.setId(1);
        Applicant applicant = new Applicant();
        List<String> skills = List.of("Java", "Spring");

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(applicantRepo.findById(user.getId())).thenReturn(Optional.of(applicant));

        boolean result = service.setSkills(username, skills);

        assertTrue(result);
        verify(userRepo,times(1)).findByUsername(username);
        verify(applicantRepo,times(1)).findById(user.getId());
        verify(applicantRepo,times(1)).save(applicant);
    }

    @Test
    void setSkills_InvalidUsername_ReturnsFalse() {
        String username = "invalidUser";
        List<String> skills = List.of("Java", "Spring");

        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = service.setSkills(username, skills);

        assertFalse(result);
        verify(userRepo,times(1)).findByUsername(username);
        verify(applicantRepo, never()).findById(anyInt());
        verify(applicantRepo, never()).save(any());
    }
}
