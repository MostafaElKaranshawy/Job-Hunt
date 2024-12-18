package com.software.backend.service;

import com.software.backend.dto.HomeDto;
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
import java.util.List;
import java.util.Optional;
import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
class HomeJobServiceTest {

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
    void testFilterJobs() {
        // Arrange
        String type = "Full_Time";
        String location = "New York";
        String category = "IT";
        String salary = "2000";
        String level = "Mid";
        String query = "Developer";
        String sort = "SalaryDesc";
        int page = 0;
        int offset = 5;
        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("employmentType", type);
        filterCriteria.put("workLocation", location);
        filterCriteria.put("category", category);
        filterCriteria.put("salary", salary);
        filterCriteria.put("level", level);
        filterCriteria.put("search", query);

        JobDto job1 = new JobDto();
        job1.setSalary(1000);
        JobDto job2 = new JobDto();
        job2.setSalary(2000);
        List<JobDto> jobDtos = List.of(job1, job2);

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        try {
            HomeDto homeDto = jobService.filterJobs(type, location, category, salary, level, query, sort, page, offset);

            Assertions.assertEquals(jobDtos.size(), homeDto.getJobs().size());
            Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
        } catch (Exception e) {
            // Assert
            e.printStackTrace();
            Assertions.fail();
        }

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

//    @Test
//    void testFilterWithNoCriteria() {
//        // Arrange
//        String type = "";
//        String location = "";
//        String category = "";
//        String salary = "0";
//        String level = "";
//        String query = "";
//        String sort = "";
//        int page = 0;
//        int offset = 5;
//
//        HashMap<String, String> filterCriteria = new HashMap<>();
//        filterCriteria.put("salary", salary);
//        filterCriteria.put("search", query);
//        filterCriteria.put("employmentType", type);
//        filterCriteria.put("workLocation", location);
//        filterCriteria.put("category", category);
//        filterCriteria.put("level", level);
//
//
//        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());
//
//        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);
//
//        // Act
//        try {
//
//            HomeDto homeDto = jobService.filterJobs(type, location, category, salary, level, query, sort, page, offset);
//            Assertions.assertEquals(jobDtos.size(), homeDto.getJobs().size());
//            Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
//        } catch (Exception e) {
//            // Assert
////            e.printStackTrace();
//            Assertions.fail();
//        }
//
//    }

    @Test
    void testFilterWithNullCriteria() {
        // Arrange
        String type = null;
        String location = null;
        String category = null;
        String salary = null;
        String level = null;
        String query = null;
        String sort = "";
        int page = 0;
        int offset = 5;

        HashMap<String, String> filterCriteria = new HashMap<>();

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        try {

            HomeDto homeDto = jobService.filterJobs(type, location, category, salary, level, query, sort, page, offset);

            // Assert
            Assertions.assertEquals(jobDtos.size(), homeDto.getJobs().size());
            Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testFilterWithTypeOnly() {
        // Arrange
        String type = "Full Time";
        String location = null;
        String category = null;
        String salary = null;
        String level = null;
        String query = null;
        String sort = "";
        int page = 0;
        int offset = 5;

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("employmentType", type);

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        try {

            HomeDto homeDto = jobService.filterJobs(type, location, category, salary, level, query, sort, page, offset);

            // Assert
            Assertions.assertEquals(jobDtos.size(), homeDto.getJobs().size());
            Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testFilterWithLocationOnly() {
        // Arrange
        String type = null;
        String location = "Remote";
        String category = null;
        String salary = null;
        String level = null;
        String query = null;
        String sort = "";
        int page = 0;
        int offset = 5;

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("workLocation", location);

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        try {

            HomeDto homeDto = jobService.filterJobs(type, location, category, salary, level, query, sort, page, offset);

            // Assert
            Assertions.assertEquals(jobDtos.size(), homeDto.getJobs().size());
            Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testFilterWithCategoryOnly() {
        // Arrange
        String type = null;
        String location = null;
        String category = "Technology";
        String salary = null;
        String level = null;
        String query = null;
        String sort = null;
        int page = 0;
        int offset = 5;

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("category", category);

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        try {

            HomeDto homeDto = jobService.filterJobs(type, location, category, salary, level, query, sort, page, offset);

            // Assert
            Assertions.assertEquals(jobDtos.size(), homeDto.getJobs().size());
            Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testFilterWithSalaryOnly() {
        // Arrange
        String type = null;
        String location = null;
        String category = null;
        String salary = "15000";
        String level = null;
        String query = null;
        String sort = "";
        int page = 0;
        int offset = 5;

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("salary", salary);

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        try {

            HomeDto homeDto = jobService.filterJobs(type, location, category, salary, level, query, sort, page, offset);

            // Assert
            Assertions.assertEquals(jobDtos.size(), homeDto.getJobs().size());
            Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testFilterWithLevelOnly() {
        // Arrange
        String type = null;
        String location = null;
        String category = null;
        String salary = null;
        String level = "SENIOR_LEVEL";
        String query = null;
        String sort = "";
        int page = 0;
        int offset = 5;

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("level", level);

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        try {

            HomeDto homeDto = jobService.filterJobs(type, location, category, salary, level, query, sort, page, offset);

            // Assert
            Assertions.assertEquals(jobDtos.size(), homeDto.getJobs().size());
            Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testFilterWithQueryOnly() {
        // Arrange
        String type = null;
        String location = null;
        String category = null;
        String salary = null;
        String level = null;
        String query = "software";
        String sort = "";
        int page = 0;
        int offset = 5;

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("search", query);

        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        try {

            HomeDto homeDto = jobService.filterJobs(type, location, category, salary, level, query, sort, page, offset);
            Assertions.assertEquals(jobDtos.size(), homeDto.getJobs().size());
            Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
        } catch (Exception e) {
            Assertions.fail();
        }

        // Assert
    }

    @Test
    void testFilterWithNoResult() {
        // Arrange
        String type = "Full Time";
        String location = "REMOTE";
        String category = "Technology";
        String salary = "15000";
        String level = "SENIOR_LEVEL";
        String query = "mostafa";
        String sort = "";
        int page = 0;
        int offset = 5;

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("employmentType", type);
        filterCriteria.put("workLocation", location);
        filterCriteria.put("category", category);
        filterCriteria.put("salary", salary);
        filterCriteria.put("level", level);
        filterCriteria.put("search", query);

        List<JobDto> jobDtos = List.of();

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        try {

            HomeDto homeDto = jobService.filterJobs(type, location, category, salary, level, query, sort, page, offset);

            // Assert
            Assertions.assertTrue(homeDto.getJobs().isEmpty());
            Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
        } catch (Exception e) {
            Assertions.fail();
        }
    }
}