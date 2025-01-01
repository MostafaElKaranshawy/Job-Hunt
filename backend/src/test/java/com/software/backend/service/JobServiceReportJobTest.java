package com.software.backend.service;

import com.software.backend.dto.*;
import com.software.backend.entity.Job;
import com.software.backend.enums.JobReportReason;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.software.backend.entity.*;
import com.software.backend.mapper.FieldMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceReportJobTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicantRepository applicantRepository;

    @Mock
    private ReportedJobRepository reportedJobRepository;

    @InjectMocks
    private JobService jobService;

    private Job mockJob;
    private User mockUser;
    private Applicant mockApplicant;
    private JobReportDTO mockJobReportDTO;
    private ReportedJob mockReportedJob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock data
        mockJob = new Job();
        mockJob.setId(1);
        mockJob.setTitle("Mock Job Title");

        mockUser = new User();
        mockUser.setUsername("testUser");

        mockApplicant = new Applicant();
        mockApplicant.setId(1);

        mockJobReportDTO = new JobReportDTO();
        mockJobReportDTO.setReason("INAPPROPRIATE_CONTENT");  // Updated to valid enum value
        mockJobReportDTO.setDescription("Inappropriate job description");

        mockReportedJob = new ReportedJob();
        mockReportedJob.setJob(mockJob);
        mockReportedJob.setApplicant(mockApplicant);
        mockReportedJob.setJobReportReason(JobReportReason.INAPPROPRIATE_CONTENT);  // Updated to valid enum
        mockReportedJob.setReportDescription(mockJobReportDTO.getDescription());
    }

    @Test
    void testReportJob_Success() {
        // Arrange
        when(jobRepository.findById(1)).thenReturn(Optional.of(mockJob));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(applicantRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockApplicant));
        when(reportedJobRepository.save(any(ReportedJob.class))).thenReturn(mockReportedJob);

        // Act
        jobService.reportJob(1, "testUser", mockJobReportDTO);

        // Assert
        verify(jobRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(applicantRepository, times(1)).findById(mockUser.getId());
        verify(reportedJobRepository, times(1)).save(any(ReportedJob.class));
    }

    @Test
    void testReportJob_JobNotFound() {
        // Arrange
        when(jobRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            jobService.reportJob(1, "testUser", mockJobReportDTO);
        });
        assertEquals("Job not found for id: 1", exception.getMessage());

        // Verify that no interactions with other repositories happened
        verify(jobRepository, times(1)).findById(1);
        verifyNoInteractions(userRepository, applicantRepository, reportedJobRepository);
    }

    @Test
    void testReportJob_UserNotFound() {
        // Arrange
        when(jobRepository.findById(1)).thenReturn(Optional.of(mockJob));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            jobService.reportJob(1, "testUser", mockJobReportDTO);
        });
        assertEquals("User not found for username: testUser", exception.getMessage());

        // Verify that no interactions with other repositories happened
        verify(jobRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findByUsername("testUser");
        verifyNoInteractions(applicantRepository, reportedJobRepository);
    }

    @Test
    void testReportJob_ApplicantNotFound() {
        // Arrange
        when(jobRepository.findById(1)).thenReturn(Optional.of(mockJob));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(applicantRepository.findById(mockUser.getId())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            jobService.reportJob(1, "testUser", mockJobReportDTO);
        });
        assertEquals("Applicant not found for user: testUser", exception.getMessage());

        // Verify that no interactions with reportedJobRepository happened
        verify(jobRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(applicantRepository, times(1)).findById(mockUser.getId());
        verifyNoInteractions(reportedJobRepository);
    }

    @Test
    void testReportJob_InvalidReportReason() {
        // Arrange
        mockJobReportDTO.setReason("INVALID_REASON");  // Invalid reason (not part of the enum)
        when(jobRepository.findById(1)).thenReturn(Optional.of(mockJob));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(applicantRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockApplicant));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            jobService.reportJob(1, "testUser", mockJobReportDTO);
        });
        assertEquals("Invalid job report reason: INVALID_REASON", exception.getMessage());

        // Verify that no interactions with reportedJobRepository happened
        verify(jobRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(applicantRepository, times(1)).findById(mockUser.getId());
        verifyNoInteractions(reportedJobRepository);
    }
}


