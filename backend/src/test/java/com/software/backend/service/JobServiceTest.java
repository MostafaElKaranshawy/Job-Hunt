package com.software.backend.service;

import com.software.backend.dto.JobDto;
import com.software.backend.entity.Job;
import com.software.backend.enums.JobStatus;
import com.software.backend.filter.JobCriteriaRunner;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.JobRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobMapper jobMapper;

    @Mock
    private JobCriteriaRunner jobCriteriaRunner;

    @InjectMocks
    private JobService jobService;

    @Test
    void testGetHomeActiveJobs() {
        // Arrange
        int page = 0;
        int offset = 5;
        Pageable pageable = PageRequest.of(page, offset);

        JobStatus status = JobStatus.OPEN;
        List<Job> jobs = List.of(new Job(), new Job());
        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobRepository.findAllByStatusIs(status, pageable)).thenReturn(Optional.of(jobs));
        Mockito.when(jobMapper.jobToJobDto(Mockito.any(Job.class))).thenReturn(new JobDto());

        // Act
        List<JobDto> result = jobService.getHomeActiveJobs(page, offset);

        // Assert
        Assertions.assertEquals(jobDtos.size(), result.size());
        Mockito.verify(jobRepository).findAllByStatusIs(status, pageable);
        Mockito.verify(jobMapper, Mockito.times(jobs.size())).jobToJobDto(Mockito.any(Job.class));
    }

    @Test
    void testSearchJobs() {
        // Arrange
        String query = "Developer";
        int page = 0;
        int offset = 5;
        Pageable pageable = PageRequest.of(page, offset);

        List<Job> jobs = List.of(new Job(), new Job());
        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobRepository.findAllByTitleContainsOrDescriptionContains(query, query, pageable)).thenReturn(Optional.of(jobs));
        Mockito.when(jobMapper.jobToJobDto(Mockito.any(Job.class))).thenReturn(new JobDto());

        // Act
        List<JobDto> result = jobService.searchJobs(query, page, offset);

        // Assert
        Assertions.assertEquals(jobDtos.size(), result.size());
        Mockito.verify(jobRepository).findAllByTitleContainsOrDescriptionContains(query, query, pageable);
        Mockito.verify(jobMapper, Mockito.times(jobs.size())).jobToJobDto(Mockito.any(Job.class));
    }

    @Test
    void testFilterJobs() {
        // Arrange
        String type = "Full-Time";
        String location = "New York";
        String category = "IT";
        String salary = "1000-2000";
        String level = "Mid";
        String query = "Developer";

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("type", type);
        filterCriteria.put("location", location);
        filterCriteria.put("category", category);
        filterCriteria.put("salary", salary);
        filterCriteria.put("level", level);
        filterCriteria.put("search", query);

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        List<JobDto> result = jobService.filterJobs(type, location, category, salary, level, query);

        // Assert
        Assertions.assertEquals(jobDtos.size(), result.size());
        Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
    }

    @Test
    void testGetHomeActiveJobsWhenRepositoryReturnsEmpty() {
        // Arrange
        int page = 0;
        int offset = 5;
        Pageable pageable = PageRequest.of(page, offset);

        JobStatus status = JobStatus.OPEN;

        Mockito.when(jobRepository.findAllByStatusIs(status, pageable)).thenReturn(Optional.empty());

        // Act
        List<JobDto> result = jobService.getHomeActiveJobs(page, offset);

        // Assert
        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(jobRepository).findAllByStatusIs(status, pageable);
    }

    @Test
    void testFilterWithNoCriteria() {
        // Arrange
        String type = "";
        String location = "";
        String category = "";
        String salary = "0";
        String level = "";
        String query = "";

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("salary", salary);
        filterCriteria.put("search", query);
        filterCriteria.put("type", type);
        filterCriteria.put("location", location);
        filterCriteria.put("category", category);
        filterCriteria.put("level", level);


        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        List<JobDto> result = jobService.filterJobs(type, location, category, salary, level, query);

        // Assert
        Assertions.assertEquals(jobDtos.size(), result.size());
        Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
    }

    @Test
    void testFilterWithNullCriteria() {
        // Arrange
        String type = null;
        String location = null;
        String category = null;
        String salary = null;
        String level = null;
        String query = null;

        HashMap<String, String> filterCriteria = new HashMap<>();

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        List<JobDto> result = jobService.filterJobs(type, location, category, salary, level, query);

        // Assert
        Assertions.assertEquals(jobDtos.size(), result.size());
        Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
    }

    @Test
    void testFilterWithTypeOnly(){
        // Arrange
        String type = "Full Time";
        String location = null;
        String category = null;
        String salary = null;
        String level = null;
        String query = null;

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("type", type);

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        List<JobDto> result = jobService.filterJobs(type, location, category, salary, level, query);

        // Assert
        Assertions.assertEquals(jobDtos.size(), result.size());
        Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
    }

    @Test
    void testFilterWithLocationOnly(){
        // Arrange
        String type = null;
        String location = "Remote";
        String category = null;
        String salary = null;
        String level = null;
        String query = null;

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("location", location);

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        List<JobDto> result = jobService.filterJobs(type, location, category, salary, level, query);

        // Assert
        Assertions.assertEquals(jobDtos.size(), result.size());
        Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
    }

    @Test
    void testFilterWithCategoryOnly(){
        // Arrange
        String type = null;
        String location = null;
        String category = "Technology";
        String salary = null;
        String level = null;
        String query = null;

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("category", category);

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        List<JobDto> result = jobService.filterJobs(type, location, category, salary, level, query);

        // Assert
        Assertions.assertEquals(jobDtos.size(), result.size());
        Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
    }

    @Test
    void testFilterWithSalaryOnly(){
        // Arrange
        String type = null;
        String location = null;
        String category = null;
        String salary = "15000";
        String level = null;
        String query = null;

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("salary", salary);

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        List<JobDto> result = jobService.filterJobs(type, location, category, salary, level, query);

        // Assert
        Assertions.assertEquals(jobDtos.size(), result.size());
        Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
    }

    @Test
    void testFilterWithLevelOnly(){
        // Arrange
        String type = null;
        String location = null;
        String category = null;
        String salary = null;
        String level = "Senior";
        String query = null;

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("level", level);

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        List<JobDto> result = jobService.filterJobs(type, location, category, salary, level, query);

        // Assert
        Assertions.assertEquals(jobDtos.size(), result.size());
        Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
    }

    @Test
    void testFilterWithQueryOnly(){
        // Arrange
        String type = null;
        String location = null;
        String category = null;
        String salary = null;
        String level = null;
        String query = "software";

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("search", query);

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        List<JobDto> result = jobService.filterJobs(type, location, category, salary, level, query);

        // Assert
        Assertions.assertEquals(jobDtos.size(), result.size());
        Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
    }

    @Test
    void testFilterWithNoResult(){
        // Arrange
        String type = "Full Time";
        String location = "Remote";
        String category = "Technology";
        String salary = "15000";
        String level = "Senior";
        String query = "mostafa";

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("type", type);
        filterCriteria.put("location", location);
        filterCriteria.put("category", category);
        filterCriteria.put("salary", salary);
        filterCriteria.put("level", level);
        filterCriteria.put("search", query);

        List<JobDto> jobDtos = List.of();

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        List<JobDto> result = jobService.filterJobs(type, location, category, salary, level, query);

        // Assert
        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
    }


}
