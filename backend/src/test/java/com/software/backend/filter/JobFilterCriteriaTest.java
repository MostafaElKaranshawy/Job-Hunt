package com.software.backend.filter;

import com.software.backend.dto.JobDto;
import com.software.backend.entity.Job;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JobFilterCriteriaTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobMapper jobMapper;

    @InjectMocks
    private JobCategoryCriteria jobCategoryCriteria;

    @InjectMocks
    private JobLevelCriteria jobLevelCriteria;

    @InjectMocks
    private JobLocationCriteria jobLocationCriteria;

    @InjectMocks
    private JobSalaryCriteria jobSalaryCriteria;

    @InjectMocks
    private JobSearchCriteria jobSearchCriteria;

    @InjectMocks
    private JobTypeCriteria jobTypeCriteria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCategoryCriteria() {
        // Arrange
        String data = "Software";
        JobDto jobDto = new JobDto();
        jobDto.setCategory("Software");

        Job job = new Job();
        job.setCategory("Software");

        List<Job> jobs = new ArrayList<>();
        jobs.add(job);

        when(jobMapper.jobDtoToJob(jobDto)).thenReturn(job);
        when(jobRepository.findAllByCategoryEqualsIgnoreCase(data)).thenReturn(Optional.of(jobs));
        when(jobMapper.jobToJobDto(job)).thenReturn(jobDto);

        // Act
        List<JobDto> result = jobCategoryCriteria.meetCriteria(data);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Software", result.get(0).getCategory());
    }

    @Test
    void testLocationCriteria() {
        // Arrange
        String location = "Remote";
        JobDto jobDto = new JobDto();
        jobDto.setLocation("Remote");

        Job job = new Job();
        job.setLocation("Remote");

        List<Job> jobs = new ArrayList<>();
        jobs.add(job);

        when(jobMapper.jobDtoToJob(jobDto)).thenReturn(job);
        when(jobRepository.findAllByLocationEqualsIgnoreCase(location)).thenReturn(Optional.of(jobs));
        when(jobMapper.jobToJobDto(job)).thenReturn(jobDto);

        // Act
        List<JobDto> result = jobLocationCriteria.meetCriteria(location);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Remote", result.get(0).getLocation());
    }

    @Test
    void testTypeCriteria() {
        // Arrange
        String type = "Full-Time";
        JobDto jobDto = new JobDto();
        jobDto.setType("Full-Time");

        Job job = new Job();
        job.setType("Full-Time");

        List<Job> jobs = new ArrayList<>();
        jobs.add(job);

        when(jobMapper.jobDtoToJob(jobDto)).thenReturn(job);
        when(jobRepository.findAllByTypeEqualsIgnoreCase(type)).thenReturn(Optional.of(jobs));
        when(jobMapper.jobToJobDto(job)).thenReturn(jobDto);

        // Act
        List<JobDto> result = jobTypeCriteria.meetCriteria(type);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Full-Time", result.get(0).getType());
    }

    @Test
    void testSalaryCriteria() {
        // Arrange
        String salaryRange = "50000";
        JobDto jobDto = new JobDto();
        jobDto.setSalary(55000);

        Job job = new Job();
        job.setSalary(55000);

        List<Job> jobs = new ArrayList<>();
        jobs.add(job);

        when(jobMapper.jobDtoToJob(jobDto)).thenReturn(job);
        when(jobRepository.findAllBySalaryGreaterThanEqual(50000)).thenReturn(Optional.of(jobs));
        when(jobMapper.jobToJobDto(job)).thenReturn(jobDto);

        // Act
        List<JobDto> result = jobSalaryCriteria.meetCriteria(salaryRange);

        // Assert
        assertEquals(1, result.size());
        assertEquals(55000, result.get(0).getSalary());
    }

    @Test
    void testLevelCriteria() {
        // Arrange
        String level = "Entry-Level";
        JobDto jobDto = new JobDto();
        jobDto.setLevel("Entry-Level");

        Job job = new Job();
        job.setLevel("Entry-Level");

        List<Job> jobs = new ArrayList<>();
        jobs.add(job);

        when(jobMapper.jobDtoToJob(jobDto)).thenReturn(job);
        when(jobRepository.findAllByLevelEqualsIgnoreCase(level)).thenReturn(Optional.of(jobs));
        when(jobMapper.jobToJobDto(job)).thenReturn(jobDto);

        // Act
        List<JobDto> result = jobLevelCriteria.meetCriteria(level);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Entry-Level", result.get(0).getLevel());
    }

    @Test
    void testSearchCriteria() {
        // Arrange
        String search = "Developer";
        JobDto jobDto = new JobDto();
        jobDto.setTitle("Developer");

        Job job = new Job();
        job.setTitle("Developer");

        List<Job> jobs = new ArrayList<>();
        jobs.add(job);

        when(jobMapper.jobDtoToJob(jobDto)).thenReturn(job);
        when(jobRepository.findAllByTitleContainsOrDescriptionContains(search, search)).thenReturn(Optional.of(jobs));
        when(jobMapper.jobToJobDto(job)).thenReturn(jobDto);

        // Act
        List<JobDto> result = jobSearchCriteria.meetCriteria(search);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Developer", result.get(0).getTitle());
    }

}
