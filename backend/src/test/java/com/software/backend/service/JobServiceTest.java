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
import com.software.backend.dto.FieldDto;
import com.software.backend.dto.JobDto;
import com.software.backend.dto.SectionDto;
import com.software.backend.entity.*;
import com.software.backend.mapper.FieldMapper;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.JobRepository;
import com.software.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Mock
    private UserRepository userRepository;

    @Mock
    private StaticSectionService staticSectionService;
    @Mock
    private FieldMapper fieldMapper;

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

        Mockito.when(jobRepository.findAllByTitleContainsOrCompany_NameContainsIgnoreCase(query, query)).thenReturn(Optional.of(jobs));
        Mockito.when(jobMapper.jobToJobDto(Mockito.any(Job.class))).thenReturn(new JobDto());

        // Act
        List<JobDto> result = jobService.searchJobs(query, page, offset);

        // Assert
        Assertions.assertEquals(jobDtos.size(), result.size());
        Mockito.verify(jobRepository).findAllByTitleContainsOrCompany_NameContainsIgnoreCase(query, query);
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

    @Test
    void testFilterWithNoCriteria() {
        // Arrange
        String type = "";
        String location = "";
        String category = "";
        String salary = "0";
        String level = "";
        String query = "";
        String sort = "";
        int page = 0;
        int offset = 5;

        HashMap<String, String> filterCriteria = new HashMap<>();
        filterCriteria.put("salary", salary);
        filterCriteria.put("search", query);
        filterCriteria.put("employmentType", type);
        filterCriteria.put("workLocation", location);
        filterCriteria.put("category", category);
        filterCriteria.put("level", level);


        List<JobDto> jobDtos = List.of(new JobDto(), new JobDto());

        Mockito.when(jobCriteriaRunner.matchCriteria(filterCriteria)).thenReturn(jobDtos);

        // Act
        try {

            HomeDto homeDto= jobService.filterJobs(type, location, category, salary, level, query, sort, page, offset);
            Assertions.assertEquals(jobDtos.size(), homeDto.getJobs().size());
            Mockito.verify(jobCriteriaRunner).matchCriteria(filterCriteria);
        } catch (Exception e) {
            // Assert
            e.printStackTrace();
            Assertions.fail();
        }

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
    void testFilterWithTypeOnly(){
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
    void testFilterWithLocationOnly(){
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
    void testFilterWithCategoryOnly(){
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
    void testFilterWithSalaryOnly(){
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
    void testFilterWithLevelOnly(){
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
    void testFilterWithQueryOnly(){
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
    void testFilterWithNoResult(){
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

    private User mockUser;
    private Company mockCompany;
    private JobDto mockJobDto;
    private Job mockJob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockCompany = new Company();
        mockUser.setCompany(mockCompany);

        mockJobDto = new JobDto();
        mockJobDto.setFields(new ArrayList<>());
        mockJobDto.setStaticSections(List.of("Personal Information"));
        mockJobDto.setSections(new ArrayList<>());

        mockJob = new Job();
        mockJob.setId(1);
    }

    private Section mockStaticSectionWithName(String name) {
        Section section = new Section();
        section.setName(name);

        List<Field> fields = new ArrayList<>();
        Field field = new Field();
        field.setLabel("Label for " + name);
        field.setType("text");
        field.setIsRequired(true);
        fields.add(field);

        section.setFields(fields);
        return section;
    }

    @Test
    void testCreateJobWithCustomForm_Success() {
        when(userRepository.findByUsername("testCompany")).thenReturn(Optional.of(mockUser));
        when(jobMapper.jobDtoToJob(mockJobDto)).thenReturn(mockJob);
        when(staticSectionService.getSection("Personal Information")).thenReturn(mockStaticSectionWithName("Personal Information"));

        Integer jobId = jobService.createJobWithCustomForm("testCompany", mockJobDto);

        assertNotNull(jobId);
        assertEquals(1, jobId);
        verify(userRepository, times(1)).findByUsername("testCompany");
        verify(jobMapper, times(1)).jobDtoToJob(mockJobDto);
        verify(jobRepository, times(1)).save(mockJob);
        verify(staticSectionService, times(1)).getSection("Personal Information");
    }

    @Test
    void testCreateJobWithCustomForm_UserNotFound() {
        when(userRepository.findByUsername("testCompany")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            jobService.createJobWithCustomForm("testCompany", mockJobDto);
        });

        assertEquals("User not found for username: testCompany", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("testCompany");
        verifyNoInteractions(jobMapper, jobRepository);
    }

    @Test
    void testCreateJobWithCustomForm_CompanyNotFound() {
        mockUser.setCompany(null);
        when(userRepository.findByUsername("testCompany")).thenReturn(Optional.of(mockUser));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            jobService.createJobWithCustomForm("testCompany", mockJobDto);
        });

        assertEquals("Company not found for user: testCompany", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("testCompany");
        verifyNoInteractions(jobMapper, jobRepository);
    }

    @Test
    void testCreateJobWithCustomForm_EmptyJobDto() {
        when(userRepository.findByUsername("testCompany")).thenReturn(Optional.of(mockUser));
        when(jobMapper.jobDtoToJob(mockJobDto)).thenReturn(mockJob);

        mockJobDto.setFields(Collections.emptyList());
        mockJobDto.setSections(Collections.emptyList());
        mockJobDto.setStaticSections(Collections.emptyList());

        Integer jobId = jobService.createJobWithCustomForm("testCompany", mockJobDto);

        assertNotNull(jobId);
        assertEquals(1, jobId);
        verify(jobRepository, times(1)).save(mockJob);
    }

    @Test
    void testCreateJobWithCustomForm_MultipleStaticSections() {
        when(userRepository.findByUsername("testCompany")).thenReturn(Optional.of(mockUser));
        when(jobMapper.jobDtoToJob(mockJobDto)).thenReturn(mockJob);

        Section personalInfoSection = mockStaticSectionWithName("Personal Information");
        Section educationSection = mockStaticSectionWithName("Education");

        when(staticSectionService.getSection("Personal Information")).thenReturn(personalInfoSection);
        when(staticSectionService.getSection("Education")).thenReturn(educationSection);

        mockJobDto.setStaticSections(List.of("Personal Information", "Education"));

        Integer jobId = jobService.createJobWithCustomForm("testCompany", mockJobDto);

        assertNotNull(jobId);
        assertEquals(1, jobId);
        verify(staticSectionService, times(1)).getSection("Personal Information");
        verify(staticSectionService, times(1)).getSection("Education");
        verify(jobRepository, times(1)).save(mockJob);
    }

    @Test
    void testCreateJobWithCustomSections() {
        SectionDto customSectionDto = new SectionDto();
        customSectionDto.setName("Custom Section");

        customSectionDto.setLabel(List.of("Field 1", "Field 2"));
        customSectionDto.setType(List.of("dropdown", "radio"));
        customSectionDto.setIsRequired(List.of(true, false));
        customSectionDto.setOptions(List.of(
                List.of("Option 1", "Option 2"),
                List.of("Option A", "Option B")
        ));

        mockJobDto.setSections(List.of(customSectionDto));

        when(userRepository.findByUsername("testCompany")).thenReturn(java.util.Optional.of(mockUser));
        when(jobMapper.jobDtoToJob(mockJobDto)).thenReturn(mockJob);

        Integer jobId = jobService.createJobWithCustomForm("testCompany", mockJobDto);

        assertNotNull(jobId);
        assertEquals(1, jobId);

        verify(jobRepository, times(1)).save(any(Job.class));
    }


    @Test
    void testCreateJobWithCustomFields() {
        FieldDto customFieldDto1 = new FieldDto("Custom Field 1", "text", true, new ArrayList<>());
        FieldDto customFieldDto2 = new FieldDto("Custom Field 2", "number", false, new ArrayList<>());
        mockJobDto.setFields(List.of(customFieldDto1, customFieldDto2));

        Field field1 = new Field();
        Field field2 = new Field();

        when(userRepository.findByUsername("testCompany")).thenReturn(java.util.Optional.of(mockUser));
        when(jobMapper.jobDtoToJob(mockJobDto)).thenReturn(mockJob);
        when(fieldMapper.fieldDtoToField(customFieldDto1)).thenReturn(field1);
        when(fieldMapper.fieldDtoToField(customFieldDto2)).thenReturn(field2);

        Integer jobId = jobService.createJobWithCustomForm("testCompany", mockJobDto);

        assertNotNull(jobId);
        assertEquals(1, jobId);
        assertEquals(2, mockJob.getFields().size());
        verify(fieldMapper, times(2)).fieldDtoToField(any(FieldDto.class));
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void testCreateJobWithStaticAndCustomSections() {
        SectionDto customSectionDto = new SectionDto();
        customSectionDto.setName("Custom Section");
        customSectionDto.setLabel(List.of("Custom Field"));
        customSectionDto.setType(List.of("checkbox"));
        customSectionDto.setIsRequired(List.of(true));
        customSectionDto.setOptions(List.of(
                List.of("Option 1", "Option 2", "Option 3")
        ));
        mockJobDto.setSections(List.of(customSectionDto));

        when(userRepository.findByUsername("testCompany")).thenReturn(java.util.Optional.of(mockUser));
        when(jobMapper.jobDtoToJob(mockJobDto)).thenReturn(mockJob);

        Section staticSection = new Section();
        staticSection.setName("Static Section");
        when(staticSectionService.getSection("Static Section")).thenReturn(staticSection);
        mockJobDto.setStaticSections(List.of("Static Section"));

        Integer jobId = jobService.createJobWithCustomForm("testCompany", mockJobDto);

        // Assert
        assertNotNull(jobId);
        assertEquals(1, jobId);
        verify(staticSectionService, times(1)).getSection("Static Section");
        verify(jobRepository, times(1)).save(any(Job.class));
    }
}
