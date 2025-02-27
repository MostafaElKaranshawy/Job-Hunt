package com.software.backend.filter;

import com.software.backend.dto.JobDto;
import com.software.backend.enums.EmploymentType;
import com.software.backend.enums.Level;
import com.software.backend.enums.WorkLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JobCriteriaRunnerTest {

    @InjectMocks
    private JobCriteriaRunner jobCriteriaRunner;

    @Mock
    private JobsFilterCriteria jobTypeCriteria;

    @Mock
    private JobsFilterCriteria jobCategoryCriteria;

    @Mock
    private JobsFilterCriteria jobLocationCriteria;

    @Mock
    private JobsFilterCriteria jobSalaryCriteria;

    @Mock
    private JobsFilterCriteria jobLevelCriteria;

    @Mock
    private JobsFilterCriteria jobSearchCriteria;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMatchCriteria() {
        jobCriteriaRunner = new JobCriteriaRunner(jobTypeCriteria, jobCategoryCriteria, jobLocationCriteria, jobSalaryCriteria, jobLevelCriteria, jobSearchCriteria);
        // Setup test data
        HashMap<String, String> criteria = new HashMap<>();
        criteria.put("employmentType", "FULL_TIME");
        criteria.put("category", "IT");

        JobDto job1 = new JobDto();
        job1.setTitle("Developer");
        job1.setEmploymentType(EmploymentType.FULL_TIME);
        job1.setCategory("Technology");
        job1.setWorkLocation(WorkLocation.REMOTE);
        job1.setSalary(50000);
        job1.setLevel(Level.ENTRY_LEVEL);

        JobDto job2 = new JobDto();
        job2.setTitle("Designer");
        job2.setEmploymentType(EmploymentType.FULL_TIME);
        job2.setCategory("IT");
        job2.setWorkLocation(WorkLocation.HYBRID);
        job2.setSalary(45000);
        job2.setLevel(Level.JUNIOR_LEVEL);

        List<JobDto> filteredJobsType = new ArrayList<>();
        List<JobDto> filteredJobsCategory = new ArrayList<>();
        filteredJobsType.add(job1);
        filteredJobsType.add(job2);
        filteredJobsCategory.add(job1);

        // Mock the meetCriteria methods
        when(jobTypeCriteria.meetCriteria("FULL_TIME")).thenReturn(filteredJobsType);
        when(jobCategoryCriteria.meetCriteria("IT")).thenReturn(filteredJobsCategory);


        List<JobDto> result = jobCriteriaRunner.matchCriteria(criteria);

        // Validate the result
        assertEquals(1, result.size());
        assertEquals(job1, result.getFirst());  // Only the 'Developer' job should match
    }

    @Test
    void testMatchCriteriaWithNoMatches() {
        jobCriteriaRunner = new JobCriteriaRunner(jobTypeCriteria, jobCategoryCriteria, jobLocationCriteria, jobSalaryCriteria, jobLevelCriteria, jobSearchCriteria);
        // Setup test data
        HashMap<String, String> criteria = new HashMap<>();
        criteria.put("employmentType", "FULL_TIME");
        criteria.put("category", "Technology");

        JobDto job1 = new JobDto();
        job1.setTitle("Developer");
        job1.setEmploymentType(EmploymentType.FULL_TIME);
        job1.setCategory("IT");
        job1.setWorkLocation(WorkLocation.REMOTE);
        job1.setSalary(50000);
        job1.setLevel(Level.ENTRY_LEVEL);

        List<JobDto> filteredJobsType = new ArrayList<>();
        filteredJobsType.add(job1);
        List<JobDto> filteredJobsCategory = new ArrayList<>();

        // Mock the meetCriteria methods
        when(jobTypeCriteria.meetCriteria("FULL_TIME")).thenReturn(filteredJobsType);
        when(jobCategoryCriteria.meetCriteria("Technology")).thenReturn(filteredJobsCategory);

        // Call the method
        List<JobDto> result = jobCriteriaRunner.matchCriteria(criteria);

        // Validate that there are no results
        assertTrue(result.isEmpty());
    }
    @Test
    void testMatchCriteriaWithLocationCriteria() {
        jobCriteriaRunner = new JobCriteriaRunner(jobTypeCriteria, jobCategoryCriteria, jobLocationCriteria, jobSalaryCriteria, jobLevelCriteria, jobSearchCriteria);
        HashMap<String, String> criteria = new HashMap<>();
        criteria.put("workLocation", "Remote");

        JobDto job1 = new JobDto();
        job1.setTitle("Developer");
        job1.setWorkLocation(WorkLocation.REMOTE);

        List<JobDto> filteredJobsLocation = new ArrayList<>();
        filteredJobsLocation.add(job1);

        when(jobLocationCriteria.meetCriteria("Remote")).thenReturn(filteredJobsLocation);

        List<JobDto> result = jobCriteriaRunner.matchCriteria(criteria);

        assertEquals(1, result.size());
        assertEquals(job1, result.getFirst());
    }

    @Test
    void testMatchCriteriaWithSalaryCriteria() {
        jobCriteriaRunner = new JobCriteriaRunner(jobTypeCriteria, jobCategoryCriteria, jobLocationCriteria, jobSalaryCriteria, jobLevelCriteria, jobSearchCriteria);
        HashMap<String, String> criteria = new HashMap<>();
        criteria.put("salary", "50000");

        JobDto job1 = new JobDto();
        job1.setTitle("Developer");
        job1.setSalary(50000);

        JobDto job2 = new JobDto();
        job2.setTitle("Designer");
        job2.setSalary(45000);

        List<JobDto> filteredJobsSalary = new ArrayList<>();
        filteredJobsSalary.add(job1);

        when(jobSalaryCriteria.meetCriteria("50000")).thenReturn(filteredJobsSalary);

        List<JobDto> result = jobCriteriaRunner.matchCriteria(criteria);

        assertEquals(1, result.size());
        assertEquals(job1, result.getFirst());
    }

    @Test
    void testMatchCriteriaWithLevelCriteria() {
        jobCriteriaRunner = new JobCriteriaRunner(jobTypeCriteria, jobCategoryCriteria, jobLocationCriteria, jobSalaryCriteria, jobLevelCriteria, jobSearchCriteria);
        HashMap<String, String> criteria = new HashMap<>();
        criteria.put("level", "Entry");

        JobDto job1 = new JobDto();
        job1.setTitle("Developer");
        job1.setLevel(Level.ENTRY_LEVEL);

        List<JobDto> filteredJobsLevel = new ArrayList<>();
        filteredJobsLevel.add(job1);

        when(jobLevelCriteria.meetCriteria("Entry")).thenReturn(filteredJobsLevel);

        List<JobDto> result = jobCriteriaRunner.matchCriteria(criteria);

        assertEquals(1, result.size());
        assertEquals(job1, result.getFirst());
    }

    @Test
    void testMatchCriteriaWithSearchCriteria() {
        jobCriteriaRunner = new JobCriteriaRunner(jobTypeCriteria, jobCategoryCriteria, jobLocationCriteria, jobSalaryCriteria, jobLevelCriteria, jobSearchCriteria);
        HashMap<String, String> criteria = new HashMap<>();
        criteria.put("search", "Developer");

        JobDto job1 = new JobDto();
        job1.setTitle("Developer");
        job1.setCategory("IT");

        List<JobDto> filteredJobsSearch = new ArrayList<>();
        filteredJobsSearch.add(job1);

        when(jobSearchCriteria.meetCriteria("Developer")).thenReturn(filteredJobsSearch);
        List<JobDto> result = jobCriteriaRunner.matchCriteria(criteria);

        assertEquals(1, result.size());
        assertEquals(job1, result.getFirst());
    }

    @Test
    void testFilterWithWrongCriteria() {
        jobCriteriaRunner = new JobCriteriaRunner(jobTypeCriteria, jobCategoryCriteria, jobLocationCriteria, jobSalaryCriteria, jobLevelCriteria, jobSearchCriteria);
        HashMap<String, String> criteria = new HashMap<>();
        criteria.put("no criteria", "no data");
        List<JobDto> result = jobCriteriaRunner.matchCriteria(criteria);

        assertEquals(0, result.size());
    }
}
