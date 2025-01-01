package com.software.backend.service;

import com.software.backend.dto.*;
import com.software.backend.entity.*;
import com.software.backend.enums.ApplicationStatus;
import com.software.backend.filter.JobCriteriaRunner;
import com.software.backend.mapper.JobApplicationMapper;
import com.software.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
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

    @Mock
    private SavedJobRepository savedJobRepository;
    @Mock
    private ApplicantRepository applicantRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobCriteriaRunner jobCriteriaRunner;

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

//    @Test
//    void testGetApplicationsByApplicantWhenNoApplications() {
//        String username = "testUser";
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
//        when(jobApplicationRepository.findApplicationsByApplicantId(1)).thenReturn(Optional.of(Arrays.asList()));
//
//        List<ApplicantApplicationsResponseDto> response = jobService.getApplicationsByApplicant(username);
//
//        assertNotNull(response);
//        assertTrue(response.isEmpty());
//    }

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
        when(jobApplicationRepository.findApplicationsByApplicantId(1)).thenReturn(Optional.of(Collections.singletonList(mockJobApplication)));

        ApplicationResponse mockApplicationResponse = new ApplicationResponse();
        mockApplicationResponse.setResponseData("I love software development");
        mockApplicationResponse.setField(new Field());

        mockJobApplication.setApplicationResponsesList(List.of(mockApplicationResponse));

        BriefApplicationResponseDto mockBriefApplicationResponseDto = new BriefApplicationResponseDto();
        mockBriefApplicationResponseDto.setFieldName("Why do you want this job?");
        mockBriefApplicationResponseDto.setResponseData("I love software development");

        when(jobApplicationMapper.toBriefApplicationResponseDto(mockApplicationResponse))
                .thenReturn(mockBriefApplicationResponseDto);

        ApplicantApplicationsResponseDto mockDto = new ApplicantApplicationsResponseDto();
        mockDto.setApplicationId(1);
        mockDto.setJobTitle("Software Engineer");
        mockDto.setResponses(List.of(mockBriefApplicationResponseDto));

        when(jobApplicationMapper.toApplicantApplicationsResponseDto(mockJobApplication)).thenReturn(mockDto);

        List<ApplicantApplicationsResponseDto> response = jobService.getApplicationsByApplicant(username);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals("Software Engineer", response.get(0).getJobTitle());
        assertEquals(1, response.get(0).getApplicationId());
        assertEquals(1, response.get(0).getResponses().size());
        assertEquals("Why do you want this job?", response.get(0).getResponses().get(0).getFieldName());
    }

//    @Test
//    void testHandleHomeJobs() {
//        String username = "testUser";
//
//        JobDto jobDto1 = new JobDto();
//        jobDto1.setId(1);
//        jobDto1.setSaved(false);
//        jobDto1.setApplied(false);
//        JobDto jobDto2 = new JobDto();
//        jobDto2.setId(1);
//        jobDto2.setSaved(false);
//        jobDto2.setApplied(false);
//        List<JobDto> jobs = new ArrayList<>();
//        jobs.add(jobDto1);
//        jobs.add(jobDto2);
//
//        HomeDto filteredJobs = new HomeDto();
//        filteredJobs.setJobs(jobs);
//
//        List<Integer> savedJobsIds = List.of(1);
//        List<Integer> appliedJobsIds = List.of(2);
//
//        User user = new User();
//        user.setId(1);
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//        when(savedJobRepository.getJobIdByApplicantId(1)).thenReturn(Optional.of(savedJobsIds));
//        when(jobApplicationRepository.getJobIdByApplicantIdAndJobIds(1, List.of(1, 2)))
//                .thenReturn(Optional.of(appliedJobsIds));
//        when(jobService.filterJobs(any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
//                .thenReturn(filteredJobs);
//
//        HomeDto result = jobService.handleHomeJobs(username, "", "", "", "", "", "", "", 1, 10);
//
//        assertEquals(2, result.getJobs().size());
//        assertTrue(result.getJobs().get(0).isSaved());
//        assertTrue(result.getJobs().get(1).isApplied());
//        verify(userRepository).findByUsername(username);
//    }

    @Test
    void testSaveJob() {
        String username = "testUser";
        int jobId = 1;
        Applicant applicant = new Applicant();
        applicant.setId(1);
        Job job = new Job();
        job.setId(1);

        when(applicantRepository.findByUser_username(username)).thenReturn(Optional.of(applicant));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(savedJobRepository.existsByApplicantIdAndJobId(applicant.getId(), jobId)).thenReturn(false);

        jobService.saveJob(username, jobId);

        ArgumentCaptor<SavedJob> captor = ArgumentCaptor.forClass(SavedJob.class);
        verify(savedJobRepository).save(captor.capture());
        SavedJob savedJob = captor.getValue();

        assertEquals(applicant, savedJob.getApplicant());
        assertEquals(job, savedJob.getJob());
        assertNotNull(savedJob.getCreatedAt());
    }

    @Test
    void testSaveJobAlreadySaved() {
        String username = "testUser";
        int jobId = 1;
        Applicant applicant = new Applicant();
        applicant.setId(1);
        when(applicantRepository.findByUser_username(username)).thenReturn(Optional.of(applicant));
        when(savedJobRepository.existsByApplicantIdAndJobId(applicant.getId(), jobId)).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> jobService.saveJob(username, jobId));
        assertEquals("Job not found with id: 1", exception.getMessage());
    }

    @Test
    void testUnSaveJob() {
        String username = "testUser";
        int jobId = 1;
        User user = new User();
        user.setId(1);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        jobService.unSaveJob(username, jobId);

        verify(savedJobRepository).deleteByApplicantIdAndJobId(user.getId(), jobId);
    }

    @Test
    void testUnSaveJobNotFound() {
        String username = "testUser";
        int jobId = 1;

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> jobService.unSaveJob(username, jobId));
        assertEquals("Applicant not found with username: testUser", exception.getMessage());
    }
}
