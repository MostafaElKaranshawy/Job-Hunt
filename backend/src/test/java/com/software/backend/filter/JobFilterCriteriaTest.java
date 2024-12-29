package com.software.backend.filter;

import com.software.backend.dto.JobDto;
import com.software.backend.entity.Job;
import com.software.backend.enums.EmploymentType;
import com.software.backend.enums.Level;
import com.software.backend.enums.WorkLocation;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

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
        when(jobRepository.findAllByCategoryContainsIgnoreCase(data)).thenReturn(Optional.of(jobs));
        when(jobMapper.jobToJobDto(job)).thenReturn(jobDto);

        // Act
        List<JobDto> result = jobCategoryCriteria.meetCriteria(data);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Software", result.getFirst().getCategory());
    }

    @Test
    void testLocationCriteria() {
        // Arrange
        String location = "REMOTE";
        JobDto jobDto = new JobDto();
        jobDto.setWorkLocation(WorkLocation.REMOTE);

        Job job = new Job();
        job.setWorkLocation(WorkLocation.REMOTE);

        List<Job> jobs = new ArrayList<>();
        jobs.add(job);

        when(jobMapper.jobDtoToJob(jobDto)).thenReturn(job);
        when(jobRepository.findAllByWorkLocationEquals(WorkLocation.valueOf(location))).thenReturn(Optional.of(jobs));
        when(jobMapper.jobToJobDto(job)).thenReturn(jobDto);

        // Act
        List<JobDto> result = jobLocationCriteria.meetCriteria(location);

        // Assert
        assertEquals(1, result.size());
        assertEquals(WorkLocation.REMOTE, result.getFirst().getWorkLocation());
    }

    @Test
    void testTypeCriteria() {
        // Arrange
        String type = "FULL_TIME";
        JobDto jobDto = new JobDto();
        jobDto.setEmploymentType(EmploymentType.FULL_TIME);

        Job job = new Job();
        job.setEmploymentType(EmploymentType.FULL_TIME);

        List<Job> jobs = new ArrayList<>();
        jobs.add(job);

        when(jobMapper.jobDtoToJob(jobDto)).thenReturn(job);
        when(jobRepository.findAllByEmploymentTypeEquals(EmploymentType.valueOf(type))).thenReturn(Optional.of(jobs));
        when(jobMapper.jobToJobDto(job)).thenReturn(jobDto);

        // Act
        List<JobDto> result = jobTypeCriteria.meetCriteria(type);

        // Assert
        assertEquals(1, result.size());
        assertEquals(EmploymentType.FULL_TIME, result.getFirst().getEmploymentType());
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
        assertEquals(55000, result.getFirst().getSalary());
    }

    @Test
    void testLevelCriteria() {
        // Arrange
        String level = "ENTRY_LEVEL";
        JobDto jobDto = new JobDto();
        jobDto.setLevel(Level.ENTRY_LEVEL);

        Job job = new Job();
        job.setLevel(Level.ENTRY_LEVEL);

        List<Job> jobs = new ArrayList<>();
        jobs.add(job);

        when(jobMapper.jobDtoToJob(jobDto)).thenReturn(job);
        when(jobRepository.findAllByLevelEquals(Level.valueOf(level))).thenReturn(Optional.of(jobs));
        when(jobMapper.jobToJobDto(job)).thenReturn(jobDto);

        // Act
        List<JobDto> result = jobLevelCriteria.meetCriteria(level);

        // Assert
        assertEquals(1, result.size());
        assertEquals(Level.ENTRY_LEVEL, result.getFirst().getLevel());
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
        when(jobRepository.findAllByTitleContainsOrCompany_NameContainsIgnoreCase(search, search)).thenReturn(Optional.of(jobs));
        when(jobMapper.jobToJobDto(job)).thenReturn(jobDto);

        // Act
        List<JobDto> result = jobSearchCriteria.meetCriteria(search);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Developer", result.getFirst().getTitle());
    }

}
