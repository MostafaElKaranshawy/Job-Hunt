package com.software.backend.service;

import com.software.backend.dto.CompanyDto;
import com.software.backend.dto.JobDto;
import com.software.backend.entity.Company;
import com.software.backend.entity.Job;
import com.software.backend.entity.User;
import com.software.backend.mapper.CompanyMapper;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.CompanyRepository;
import com.software.backend.repository.JobRepository;
import com.software.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyServiceTest {

    @InjectMocks
    private CompanyService service;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobMapper jobMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCompanyInfo_ValidCompanyUsername() {
        String companyUsername = "testCompany";
        User user = new User();
        Company company = new Company();
        user.setId(1);
        user.setUsername(companyUsername);
        user.setCompany(company);
        company.setUser(user);
        CompanyDto companyDto = new CompanyDto();

        when(userRepository.findByUsername(companyUsername)).thenReturn(Optional.of(user));
        when(companyMapper.toCompanyDTO(company)).thenReturn(companyDto);

        CompanyDto result = service.getCompanyInfo(companyUsername);

        assertNotNull(result);
        assertEquals(companyDto, result);
        verify(userRepository, times(1)).findByUsername(companyUsername);
    }

    @Test
    void getCompanyInfo_InvalidCompanyUsername_ReturnsNull() {
        String companyUsername = "invalidCompany";

        when(userRepository.findByUsername(companyUsername)).thenReturn(Optional.empty());

        CompanyDto result = service.getCompanyInfo(companyUsername);

        assertNull(result);
        verify(userRepository, times(1)).findByUsername(companyUsername);
        verify(companyRepository, never()).save(any());
    }

    @Test
    void updateCompanyInfo_ValidCompanyUsername() {
        String companyUsername = "testCompany";
        CompanyDto companyDto = new CompanyDto();
        User user = new User();
        Company company = new Company();
        user.setId(1);
        user.setUsername(companyUsername);
        user.setCompany(company);
        company.setUser(user);

        when(userRepository.findByUsername(companyUsername)).thenReturn(Optional.of(user));
        when(companyMapper.toCompanyDTO(company)).thenReturn(companyDto);

        CompanyDto result = service.updateCompanyInfo(companyUsername, companyDto);

        assertNotNull(result);
        verify(userRepository, times(1)).findByUsername(companyUsername);
    }

    @Test
    void updateCompanyInfo_InvalidCompanyUsername_ReturnsNull() {
        String companyUsername = "invalidCompany";
        CompanyDto companyDto = new CompanyDto();

        when(userRepository.findByUsername(companyUsername)).thenReturn(Optional.empty());

        CompanyDto result = service.updateCompanyInfo(companyUsername, companyDto);

        assertNull(result);
        verify(userRepository, times(1)).findByUsername(companyUsername);
        verify(companyRepository, never()).save(any());
    }

//    @Test
//    void getCompanyJobs_ValidCompanyUsername() {
//        String companyUsername = "testCompany";
//        User user = new User();
//        Company company = new Company();
//        user.setId(1);
//        user.setUsername(companyUsername);
//        user.setCompany(company);
//        company.setUser(user);
//
//        Job job1 = new Job();
//        job1.setId(1);
//        job1.setCompany(company);
//        Job job2 = new Job();
//        job2.setId(2);
//        job2.setCompany(company);
//
//        JobDto jobDto1 = new JobDto();
//        JobDto jobDto2 = new JobDto();
//
//        when(userRepository.findByUsername(companyUsername)).thenReturn(Optional.of(user));
//        when(jobRepository.findByCompanyAndApplicationDeadlineBefore(company, LocalDateTime.now())).thenReturn(Optional.of(List.of(job1)));
//        when(jobRepository.findByCompanyAndApplicationDeadlineAfter(company, LocalDateTime.now())).thenReturn(Optional.of(List.of(job2)));
//        when(jobMapper.jobToJobDto(job1)).thenReturn(jobDto1);
//        when(jobMapper.jobToJobDto(job2)).thenReturn(jobDto2);
//
//        Map<String, List<JobDto>> result = service.getCompanyJobs(companyUsername);
//
//        assertNotNull(result);
//        assertTrue(result.containsKey("active"));
//        assertTrue(result.containsKey("expired"));
//        System.out.println(result);
//        assertEquals(1, result.get("expired").size());
//        assertEquals(1, result.get("active").size());
//        verify(userRepository, times(1)).findByUsername(companyUsername);
//        verify(jobRepository, times(1)).findByCompanyAndApplicationDeadlineBefore(company, LocalDateTime.now());
//        verify(jobRepository, times(1)).findByCompanyAndApplicationDeadlineAfter(company, LocalDateTime.now());
//    }

    @Test
    void getCompanyJobs_InvalidCompanyUsername_ReturnsNull() {
        String companyUsername = "invalidCompany";

        when(userRepository.findByUsername(companyUsername)).thenReturn(Optional.empty());

        Map<String, List<JobDto>> result = service.getCompanyJobs(companyUsername);

        assertNull(result);
        verify(userRepository, times(1)).findByUsername(companyUsername);
        verify(jobRepository, never()).findByCompanyAndApplicationDeadlineBefore(any(), any());
        verify(jobRepository, never()).findByCompanyAndApplicationDeadlineAfter(any(), any());
    }

    @Test
    void deleteJob_ValidJobId_ReturnsTrue() {
        String companyUsername = "testCompany";
        Integer jobId = 1;
        User user = new User();
        Company company = new Company();
        user.setId(1);
        user.setUsername(companyUsername);
        user.setCompany(company);
        company.setUser(user);
        Job job = new Job();
        job.setCompany(company);

        when(userRepository.findByUsername(companyUsername)).thenReturn(Optional.of(user));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        boolean result = service.deleteJob(companyUsername, jobId);

        assertTrue(result);
        verify(jobRepository, times(1)).findById(jobId);
        verify(jobRepository, times(1)).delete(job);
    }

    @Test
    void deleteJob_InvalidJobId_ReturnsFalse() {
        String companyUsername = "testCompany";
        Integer jobId = 1;

        when(userRepository.findByUsername(companyUsername)).thenReturn(Optional.empty());

        boolean result = service.deleteJob(companyUsername, jobId);

        assertFalse(result);
        verify(userRepository, times(1)).findByUsername(companyUsername);
        verify(jobRepository, never()).findById(jobId);
        verify(jobRepository, never()).delete(any());
    }
}
