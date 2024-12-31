package com.software.backend.service;

import com.software.backend.dto.ApplicantDTO;
import com.software.backend.dto.JobDto;
import com.software.backend.dto.ReportedApplicantDto;
import com.software.backend.dto.ReportedJobDto;
import com.software.backend.entity.Job;
import com.software.backend.entity.ReportedApplicant;
import com.software.backend.entity.ReportedJob;
import com.software.backend.entity.User;
import com.software.backend.enums.ApplicantReportReason;
import com.software.backend.enums.JobReportReason;
import com.software.backend.enums.WorkLocation;
import com.software.backend.exception.BusinessException;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.mapper.ReportedApplicantMapper;
import com.software.backend.mapper.ReportedJobMapper;
import com.software.backend.repository.JobRepository;
import com.software.backend.repository.ReportedApplicantRepository;
import com.software.backend.repository.ReportedJobRepository;
import com.software.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReportedJobRepository reportedJobRepository;

    @Mock
    private ReportedApplicantRepository reportedApplicantRepository;

    @Mock
    private ReportedJobMapper reportedJobMapper;

    @Mock
    private ReportedApplicantMapper reportedApplicantMapper;

    @Mock
    private JobRepository jobRepository;

    private User testUser;


    @Test
    void testBanUser_UserExists() {
        // Mock User with id 1
        testUser = new User();
        testUser.setId(1);
        testUser.setIsBanned(false);

        // Mock the behavior of the userRepository
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));


        boolean result = adminService.banUser(1);

        assertTrue(result);
        assertTrue(testUser.getIsBanned());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testBanUser_UserNotFound() {
        // Mock User with id 1
        testUser = new User();
        testUser.setId(1);
        testUser.setIsBanned(false);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> adminService.banUser(1));

        assertEquals("User not found with id: " + testUser.getId(), exception.getMessage());
    }

    @Test
    void testGetReportedJobs_EmptyList() {
        Page<ReportedJob> page = new PageImpl<>(List.of(new ReportedJob()));
        when(reportedJobRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(reportedJobMapper.toDto(any(ReportedJob.class))).thenReturn(new ReportedJobDto());

        Page<ReportedJobDto> result = adminService.getReportedJobs(0, 10);

        assertNotNull(result);
        verify(reportedJobRepository, times(1)).findAll(any(Pageable.class));
        verify(reportedJobMapper, atLeastOnce()).toDto(any(ReportedJob.class));
    }

    @Test
    void testGetReportedApplicants_EmptyList() {
        Page<ReportedApplicant> page = new PageImpl<>(List.of(new ReportedApplicant()));
        when(reportedApplicantRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(reportedApplicantMapper.toDto(any(ReportedApplicant.class))).thenReturn(new ReportedApplicantDto());

        Page<ReportedApplicantDto> result = adminService.getReportedApplicants(0, 10);

        assertNotNull(result);
        verify(reportedApplicantRepository, times(1)).findAll(any(Pageable.class));
        verify(reportedApplicantMapper, atLeastOnce()).toDto(any(ReportedApplicant.class));
    }

    @Test
    // testing correct usage of the mappers
    void testGetReportedJobs_NotEmptyList() {
        // Create mock data for ReportedJob
        ReportedJob reportedJob = new ReportedJob();
        reportedJob.setId(1);
        reportedJob.setJobReportReason(JobReportReason.SCAM);
        reportedJob.setReportDescription("Scam job posting");
        reportedJob.setCreatedAt(LocalDateTime.of(2024, 12, 31, 11, 0));

        // Mock JobDto and ApplicantDTO
        JobDto jobDto = new JobDto();
        jobDto.setId(101);
        jobDto.setTitle("Software Engineer");
        jobDto.setDescription("Develop software applications");
        jobDto.setCategory("IT");
        jobDto.setLocation("New York");
        jobDto.setWorkLocation(WorkLocation.REMOTE);
        jobDto.setPostedAt(LocalDateTime.of(2023, 12, 1, 9, 0));
        jobDto.setApplicationDeadline(LocalDateTime.of(2025, 1, 1, 8, 30));

        ApplicantDTO applicantDto = new ApplicantDTO();
        applicantDto.setId(1001);
        applicantDto.setUsername("john_doe");
        applicantDto.setFirstName("John");
        applicantDto.setLastName("Doe");
        applicantDto.setPhoneNumber("1234567890");
        applicantDto.setAddress("123 Main St");
        applicantDto.setCity("New York");
        applicantDto.setState("NY");
        applicantDto.setCountry("USA");

        // Mock ReportedJobDto mapping
        ReportedJobDto reportedJobDto = new ReportedJobDto();
        reportedJobDto.setId(1);
        reportedJobDto.setJobReportReason(JobReportReason.SCAM);
        reportedJobDto.setReportDescription("Scam job posting");
        reportedJobDto.setCreatedAt(LocalDateTime.of(2024, 12, 31, 11, 0));
        reportedJobDto.setJob(jobDto);
        reportedJobDto.setApplicant(applicantDto);

        // Mock behavior of the repository and mapper
        Page<ReportedJob> page = new PageImpl<>(List.of(reportedJob));
        when(reportedJobRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(reportedJobMapper.toDto(reportedJob)).thenReturn(reportedJobDto);

        // Call the service method
        Page<ReportedJobDto> result = adminService.getReportedJobs(0, 10);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        ReportedJobDto resultDto = result.getContent().getFirst();

        // Assert all fields match
        assertEquals(reportedJobDto.getId(), resultDto.getId());
        assertEquals(reportedJobDto.getJobReportReason(), resultDto.getJobReportReason());
        assertEquals(reportedJobDto.getReportDescription(), resultDto.getReportDescription());
        assertEquals(reportedJobDto.getCreatedAt(), resultDto.getCreatedAt());

        assertEquals(reportedJobDto.getJob().getId(), resultDto.getJob().getId());
        assertEquals(reportedJobDto.getJob().getTitle(), resultDto.getJob().getTitle());
        assertEquals(reportedJobDto.getJob().getDescription(), resultDto.getJob().getDescription());
        assertEquals(reportedJobDto.getJob().getCategory(), resultDto.getJob().getCategory());
        assertEquals(reportedJobDto.getJob().getLocation(), resultDto.getJob().getLocation());
        assertEquals(reportedJobDto.getJob().getWorkLocation(), resultDto.getJob().getWorkLocation());
        assertEquals(reportedJobDto.getJob().getPostedAt(), resultDto.getJob().getPostedAt());
        assertEquals(reportedJobDto.getJob().getApplicationDeadline(), resultDto.getJob().getApplicationDeadline());

        assertEquals(reportedJobDto.getApplicant().getId(), resultDto.getApplicant().getId());
        assertEquals(reportedJobDto.getApplicant().getUsername(), resultDto.getApplicant().getUsername());
        assertEquals(reportedJobDto.getApplicant().getFirstName(), resultDto.getApplicant().getFirstName());
        assertEquals(reportedJobDto.getApplicant().getLastName(), resultDto.getApplicant().getLastName());
        assertEquals(reportedJobDto.getApplicant().getPhoneNumber(), resultDto.getApplicant().getPhoneNumber());
        assertEquals(reportedJobDto.getApplicant().getAddress(), resultDto.getApplicant().getAddress());
        assertEquals(reportedJobDto.getApplicant().getCity(), resultDto.getApplicant().getCity());
        assertEquals(reportedJobDto.getApplicant().getState(), resultDto.getApplicant().getState());
        assertEquals(reportedJobDto.getApplicant().getCountry(), resultDto.getApplicant().getCountry());

        // Verify interactions
        verify(reportedJobRepository, times(1)).findAll(any(Pageable.class));
        verify(reportedJobMapper, times(1)).toDto(reportedJob);
    }


    @Test
    // testing correct usage of the mappers
    void testGetReportedApplicants_NotEmptyList() {
        // Create mock data for ReportedApplicant
        ReportedApplicant reportedApplicant = new ReportedApplicant();
        reportedApplicant.setId(1);
        reportedApplicant.setApplicantReportReason(ApplicantReportReason.SPAM);
        reportedApplicant.setReportDescription("Scam job posting");
        reportedApplicant.setCreatedAt(LocalDateTime.of(2024, 12, 31, 11, 0));

        // Mock ApplicantDTO
        ApplicantDTO applicantDto = new ApplicantDTO();
        applicantDto.setId(1001);
        applicantDto.setUsername("john_doe");
        applicantDto.setFirstName("John");
        applicantDto.setLastName("Doe");
        applicantDto.setPhoneNumber("1234567890");
        applicantDto.setAddress("123 Main St");
        applicantDto.setCity("New York");
        applicantDto.setState("NY");
        applicantDto.setCountry("USA");

        // Mock ReportedApplicantDto mapping
        ReportedApplicantDto reportedApplicantDto = new ReportedApplicantDto();
        reportedApplicantDto.setId(1);
        reportedApplicantDto.setApplicantReportReason(ApplicantReportReason.SPAM);
        reportedApplicantDto.setReportDescription("Scam job posting");
        reportedApplicantDto.setCreatedAt(LocalDateTime.of(2024, 12, 31, 11, 0));
        reportedApplicantDto.setApplicant(applicantDto);

        // Mock behavior of the repository and mapper
        Page<ReportedApplicant> page = new PageImpl<>(List.of(reportedApplicant));
        when(reportedApplicantRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(reportedApplicantMapper.toDto(reportedApplicant)).thenReturn(reportedApplicantDto);

        // Call the service method
        Page<ReportedApplicantDto> result = adminService.getReportedApplicants(0, 10);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        ReportedApplicantDto resultDto = result.getContent().getFirst();

        // Assert all fields match
        assertEquals(reportedApplicantDto.getId(), resultDto.getId());
        assertEquals(reportedApplicantDto.getApplicantReportReason(), resultDto.getApplicantReportReason());
        assertEquals(reportedApplicantDto.getReportDescription(), resultDto.getReportDescription());
        assertEquals(reportedApplicantDto.getCreatedAt(), resultDto.getCreatedAt());

        assertEquals(reportedApplicantDto.getApplicant().getId(), resultDto.getApplicant().getId());
        assertEquals(reportedApplicantDto.getApplicant().getUsername(), resultDto.getApplicant().getUsername());
        assertEquals(reportedApplicantDto.getApplicant().getFirstName(), resultDto.getApplicant().getFirstName());
        assertEquals(reportedApplicantDto.getApplicant().getLastName(), resultDto.getApplicant().getLastName());
        assertEquals(reportedApplicantDto.getApplicant().getPhoneNumber(), resultDto.getApplicant().getPhoneNumber());
        assertEquals(reportedApplicantDto.getApplicant().getAddress(), resultDto.getApplicant().getAddress());
        assertEquals(reportedApplicantDto.getApplicant().getCity(), resultDto.getApplicant().getCity());
        assertEquals(reportedApplicantDto.getApplicant().getState(), resultDto.getApplicant().getState());
        assertEquals(reportedApplicantDto.getApplicant().getCountry(), resultDto.getApplicant().getCountry());
    }


    @Test
    void testDeleteReportedJob_Exists() {
        ReportedJob reportedJob = new ReportedJob();
        reportedJob.setId(1);

        when(reportedJobRepository.existsById(reportedJob.getId())).thenReturn(true);

        boolean result = adminService.deleteReportedJob(reportedJob.getId());

        assertTrue(result);
        verify(reportedJobRepository, times(1)).deleteById(reportedJob.getId());
    }

    @Test
    void testDeleteReportedJob_NotExists() {
        ReportedJob reportedJob = new ReportedJob();
        reportedJob.setId(1);

        when(reportedJobRepository.existsById(reportedJob.getId())).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> adminService.deleteReportedJob(reportedJob.getId()));

        assertEquals("Reported job not found with id: " + reportedJob.getId(),
                exception.getMessage());
    }

    @Test
    void testDeleteReportedApplicant_Exists() {
        ReportedApplicant reportedApplicant = new ReportedApplicant();
        reportedApplicant.setId(1);

        when(reportedApplicantRepository.existsById(reportedApplicant.getId())).thenReturn(true);

        boolean result = adminService.deleteReportedApplicant(reportedApplicant.getId());

        assertTrue(result);
        verify(reportedApplicantRepository, times(1)).deleteById(reportedApplicant.getId());
    }

    @Test
    void testDeleteReportedApplicant_NotExists() {
        ReportedApplicant reportedApplicant = new ReportedApplicant();
        reportedApplicant.setId(1);

        when(reportedApplicantRepository.existsById(reportedApplicant.getId())).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> adminService.deleteReportedApplicant(reportedApplicant.getId()));

        assertEquals("Reported applicant not found with id: " + reportedApplicant.getId(),
                exception.getMessage());
    }


    @Test
    void testDeleteJob_Exists() {
        Job job = new Job();
        job.setId(1);

        when(jobRepository.existsById(job.getId())).thenReturn(true);

        boolean result = adminService.deleteJob(job.getId());

        assertTrue(result);
        verify(jobRepository, times(1)).deleteById(job.getId());
    }

    @Test
    void testDeleteJob_NotExists() {
        Job job = new Job();
        job.setId(1);

        when(jobRepository.existsById(job.getId())).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> adminService.deleteJob(job.getId()));

        assertEquals("Job not found with id: " + job.getId(),
                exception.getMessage());
    }
}
