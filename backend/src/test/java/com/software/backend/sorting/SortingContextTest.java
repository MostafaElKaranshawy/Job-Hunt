package com.software.backend.sorting;

import com.software.backend.dto.JobDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SortingContextTest {

    @Test
    void testSortByDateDescending() {
        String sort = "DateDesc";
        SortingContext sortingContext = new SortingContext(sort);
        JobDto job1 = new JobDto();
        job1.setPostedAt(LocalDateTime.now());

        JobDto job2 = new JobDto();
        job2.setPostedAt(LocalDateTime.now().minusDays(5));

        JobDto job3 = new JobDto();
        job3.setPostedAt(LocalDateTime.now().minusDays(10));

        JobDto job4 = new JobDto();
        job4.setPostedAt(LocalDateTime.now().minusDays(15));

        List<JobDto> jobs = new ArrayList<>();
        jobs.add(job4);
        jobs.add(job3);
        jobs.add(job2);
        jobs.add(job1);

        List<JobDto> trueSortedJobs = new ArrayList<>();
        trueSortedJobs.add(job1);
        trueSortedJobs.add(job2);
        trueSortedJobs.add(job3);
        trueSortedJobs.add(job4);

        List<JobDto> sortedJobs = sortingContext.sortJobs(jobs);

        assertEquals(sortedJobs, trueSortedJobs);
    }

    @Test
    void testSortByDateAscending() {
        String sort = "DateAsc";
        SortingContext sortingContext = new SortingContext(sort);
        JobDto job1 = new JobDto();
        job1.setPostedAt(LocalDateTime.now());

        JobDto job2 = new JobDto();
        job2.setPostedAt(LocalDateTime.now().minusDays(5));

        JobDto job3 = new JobDto();
        job3.setPostedAt(LocalDateTime.now().minusDays(10));

        JobDto job4 = new JobDto();
        job4.setPostedAt(LocalDateTime.now().minusDays(15));

        List<JobDto> jobs = new ArrayList<>();
        jobs.add(job2);
        jobs.add(job3);
        jobs.add(job4);
        jobs.add(job1);

        List<JobDto> trueSortedJobs = new ArrayList<>();
        trueSortedJobs.add(job4);
        trueSortedJobs.add(job3);
        trueSortedJobs.add(job2);
        trueSortedJobs.add(job1);

        List<JobDto> sortedJobs = sortingContext.sortJobs(jobs);

        assertEquals(sortedJobs, trueSortedJobs);
    }

    @Test
    void testSortBySalaryDescending() {
        String sort = "SalaryDesc";
        SortingContext sortingContext = new SortingContext(sort);
        JobDto job1 = new JobDto();
        job1.setSalary(1000);

        JobDto job2 = new JobDto();
        job2.setSalary(2000);

        JobDto job3 = new JobDto();
        job3.setSalary(3000);

        JobDto job4 = new JobDto();
        job4.setSalary(4000);

        List<JobDto> jobs = new ArrayList<>();
        jobs.add(job1);
        jobs.add(job3);
        jobs.add(job2);
        jobs.add(job4);

        List<JobDto> trueSortedJobs = new ArrayList<>();
        trueSortedJobs.add(job4);
        trueSortedJobs.add(job3);
        trueSortedJobs.add(job2);
        trueSortedJobs.add(job1);

        List<JobDto> sortedJobs = sortingContext.sortJobs(jobs);

        assertEquals(sortedJobs, trueSortedJobs);
    }

    @Test
    void testSortBySalaryAscending() {
        String sort = "SalaryAsc";
        SortingContext sortingContext = new SortingContext(sort);
        JobDto job1 = new JobDto();
        job1.setSalary(1000);

        JobDto job2 = new JobDto();
        job2.setSalary(2000);

        JobDto job3 = new JobDto();
        job3.setSalary(3000);

        JobDto job4 = new JobDto();
        job4.setSalary(4000);

        List<JobDto> jobs = new ArrayList<>();
        jobs.add(job4);
        jobs.add(job3);
        jobs.add(job2);
        jobs.add(job1);

        List<JobDto> trueSortedJobs = new ArrayList<>();
        trueSortedJobs.add(job1);
        trueSortedJobs.add(job2);
        trueSortedJobs.add(job3);
        trueSortedJobs.add(job4);

        List<JobDto> sortedJobs = sortingContext.sortJobs(jobs);

        assertEquals(sortedJobs, trueSortedJobs);
    }
}
