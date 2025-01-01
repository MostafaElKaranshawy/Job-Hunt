package com.software.backend.service;

import com.software.backend.dto.ApplicantApplicationsResponseDto;
import com.software.backend.dto.ApplicationResponseDTO;
import com.software.backend.entity.ApplicationResponse;
import com.software.backend.entity.Field;
import com.software.backend.entity.JobApplication;
import com.software.backend.entity.User;
import com.software.backend.enums.ApplicationStatus;
import com.software.backend.mapper.JobApplicationMapper;
import com.software.backend.repository.JobApplicationRepository;
import com.software.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class JobServiceTest {

    @InjectMocks
    private JobService jobService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private JobApplicationMapper jobApplicationMapper;

    private User mockUser;
    private JobApplication mockJobApplication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("testUser");

        mockJobApplication = new JobApplication();
        mockJobApplication.setId(1);
        mockJobApplication.setApplicationStatus(ApplicationStatus.PENDING);
    }

    @Test
    void testGetApplicationsByApplicantWhenNoApplications() {
        String username = "testUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(jobApplicationRepository.findApplicationsByApplicantId(1)).thenReturn(Optional.of(Arrays.asList()));

        List<ApplicantApplicationsResponseDto> response = jobService.getApplicationsByApplicant(username);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testGetApplicationsByApplicantWhenUserNotFound() {
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            jobService.getApplicationsByApplicant(username);
        });
        assertEquals("Applicant not found with username: nonExistingUser", thrown.getMessage());
    }

    @Test
    void testGetApplicationsByApplicantWithResponses() {
        String username = "testUserWithResponses";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(jobApplicationRepository.findApplicationsByApplicantId(1)).thenReturn(Optional.of(Arrays.asList(mockJobApplication)));

        ApplicationResponse mockApplicationResponse = new ApplicationResponse();
        mockApplicationResponse.setResponseData("I love software development");
        mockApplicationResponse.setField(new Field());

        mockJobApplication.setApplicationResponsesList(Arrays.asList(mockApplicationResponse));

        ApplicationResponseDTO mockResponseDTO = new ApplicationResponseDTO();
        mockResponseDTO.setFieldName("Why do you want this job?");
        mockResponseDTO.setResponseData("I love software development");

        when(jobApplicationMapper.toApplicationResponseDTO(mockApplicationResponse))
                .thenReturn(mockResponseDTO);

        ApplicantApplicationsResponseDto mockDto = new ApplicantApplicationsResponseDto();
        mockDto.setApplicationId(1);
        mockDto.setJobTitle("Software Engineer");
        mockDto.setResponses(Arrays.asList(mockResponseDTO));

        when(jobApplicationMapper.toApplicantApplicationsResponseDto(mockJobApplication)).thenReturn(mockDto);

        List<ApplicantApplicationsResponseDto> response = jobService.getApplicationsByApplicant(username);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals("Software Engineer", response.get(0).getJobTitle());
        assertEquals(1, response.get(0).getApplicationId());
        assertEquals(1, response.get(0).getResponses().size());
        assertEquals("Why do you want this job?", response.get(0).getResponses().get(0).getFieldName());
    }

}
