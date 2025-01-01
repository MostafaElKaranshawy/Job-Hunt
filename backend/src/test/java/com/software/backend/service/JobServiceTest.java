package com.software.backend.service;

import com.software.backend.dto.*;
import com.software.backend.entity.*;
import com.software.backend.enums.ApplicationStatus;
import com.software.backend.enums.EmploymentType;
import com.software.backend.enums.Level;
import com.software.backend.enums.WorkLocation;
import com.software.backend.filter.JobCriteriaRunner;
import com.software.backend.mapper.JobApplicationMapper;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
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
    private JobMapper jobMapper;

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

    @Test
    void testJobToJobDto() {
        jobMapper = Mappers.getMapper(JobMapper.class);

        Company company = new Company();
        company.setId(1);
        company.setName("Test Company");
        company.setLocation("Test Location");
        company.setWebsite("https://test.com");
        company.setOverview("Overview");
        company.setEstablishmentDate(LocalDate.now());
        company.setUser(new User());


        Section section = new Section();
        section.setName("Section1");



        Field field = new Field();
        field.setType("Text");
        field.setLabel("Label");
        field.setIsRequired(true);
        field.setOptions(Arrays.asList("Option1", "Option2"));


        Job job = new Job();
        job.setId(1);
        job.setTitle("Software Engineer");
        job.setDescription("Develop software");
        job.setCategory("IT");
        job.setEmploymentType(EmploymentType.FULL_TIME);
        job.setLevel(Level.SENIOR_LEVEL);
        job.setCompany(company);
        job.setSections(Arrays.asList(section));
        job.setFields(Arrays.asList(field));
        job.setSalary(50000);
        job.setWorkLocation(WorkLocation.ONSITE);


        JobDto jobDto = jobMapper.jobToJobDto(job);

        assertNotNull(jobDto);
        assertEquals(job.getTitle(), jobDto.getTitle());
        assertEquals(job.getCompany().getName(), jobDto.getCompany().getName());
        assertEquals(job.getSections().size(), jobDto.getSections().size());
        assertEquals(job.getFields().get(0).getLabel(), jobDto.getFields().get(0).getLabel());
        assertEquals(job.getEmploymentType(), jobDto.getEmploymentType());
        assertEquals(job.getLevel(), jobDto.getLevel());
        assertEquals(job.getWorkLocation(), jobDto.getWorkLocation());
        assertEquals(job.getSalary(), jobDto.getSalary());
        assertEquals(job.getCompany().getWebsite(), jobDto.getCompany().getWebsite());
        assertEquals(job.getCompany().getOverview(), jobDto.getCompany().getOverview());
        assertEquals(job.getCompany().getLocation(), jobDto.getCompany().getLocation());
    }

    @Test
    void testJobDtoToJob() {
        jobMapper = Mappers.getMapper(JobMapper.class);

        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(1);
        companyDto.setName("Test Company");
        companyDto.setLocation("Test Location");
        companyDto.setWebsite("https://test.com");
        companyDto.setOverview("Overview");

        SectionDto sectionDto = new SectionDto();
        sectionDto.setName("Section1");

        FieldDto fieldDto = new FieldDto();
        fieldDto.setType("Text");
        fieldDto.setLabel("Label");
        fieldDto.setIsRequired(true);
        fieldDto.setOptions(Arrays.asList("Option1", "Option2"));

        JobDto jobDto = new JobDto();
        jobDto.setId(1);
        jobDto.setTitle("Software Engineer");
        jobDto.setDescription("Develop software");
        jobDto.setCategory("IT");
        jobDto.setEmploymentType(EmploymentType.FULL_TIME);
        jobDto.setLevel(Level.SENIOR_LEVEL);
        jobDto.setCompany(companyDto);
        jobDto.setSections(Arrays.asList(sectionDto));
        jobDto.setFields(Arrays.asList(fieldDto));
        jobDto.setSalary(50000);
        jobDto.setWorkLocation(WorkLocation.ONSITE);

        Job job = jobMapper.jobDtoToJob(jobDto);

        assertNotNull(job);
        assertEquals(jobDto.getTitle(), job.getTitle());
        assertEquals(jobDto.getCompany().getName(), job.getCompany().getName());
        assertEquals(jobDto.getSections().size(), job.getSections().size());
        assertEquals(jobDto.getFields().get(0).getLabel(), job.getFields().get(0).getLabel());
        assertEquals(jobDto.getEmploymentType(), job.getEmploymentType());
        assertEquals(jobDto.getLevel(), job.getLevel());
        assertEquals(jobDto.getWorkLocation(), job.getWorkLocation());
        assertEquals(jobDto.getSalary(), job.getSalary());
        assertEquals(jobDto.getCompany().getWebsite(), job.getCompany().getWebsite());
        assertEquals(jobDto.getCompany().getOverview(), job.getCompany().getOverview());
        assertEquals(jobDto.getCompany().getLocation(), job.getCompany().getLocation());
    }


    @Test
    void testPersonalDataDTO() {
        ApplicationResponseDTO.PersonalDataDTO personalData = new ApplicationResponseDTO.PersonalDataDTO();
        personalData.setFullName("John Doe");
        personalData.setAddress("123 Main St");
        personalData.setPhoneNumber("123-456-7890");
        personalData.setPersonalEmail("john.doe@example.com");
        personalData.setPortfolioURL("https://portfolio.example.com");
        personalData.setLinkedInURL("https://linkedin.com/in/johndoe");
        personalData.setDateOfBirth("1990-01-01");

        assertEquals("John Doe", personalData.getFullName());
        assertEquals("123 Main St", personalData.getAddress());
        assertEquals("123-456-7890", personalData.getPhoneNumber());
        assertEquals("john.doe@example.com", personalData.getPersonalEmail());
        assertEquals("https://portfolio.example.com", personalData.getPortfolioURL());
        assertEquals("https://linkedin.com/in/johndoe", personalData.getLinkedInURL());
        assertEquals("1990-01-01", personalData.getDateOfBirth());
    }

    @Test
    void testEducationDataDTO() {
        ApplicationResponseDTO.EducationDataDTO educationData = new ApplicationResponseDTO.EducationDataDTO();
        educationData.setFieldOfStudy("Computer Science");
        educationData.setGraduationYear("2012");
        educationData.setHighestDegree("Bachelor's");
        educationData.setStartYear("2008");
        educationData.setUniversity("Example University");

        assertEquals("Computer Science", educationData.getFieldOfStudy());
        assertEquals("2012", educationData.getGraduationYear());
        assertEquals("Bachelor's", educationData.getHighestDegree());
        assertEquals("2008", educationData.getStartYear());
        assertEquals("Example University", educationData.getUniversity());
    }

    @Test
    void testExperienceDataDTO() {
        ApplicationResponseDTO.ExperienceDataDTO experienceData = new ApplicationResponseDTO.ExperienceDataDTO();
        experienceData.setCompanyName("TechCorp");
        experienceData.setJobTitle("Software Engineer");
        experienceData.setJobLocation("Remote");
        experienceData.setJobDescription("Developing software");
        experienceData.setStartDate("2020-01-01");
        experienceData.setEndDate("2022-01-01");
        experienceData.setCurrentRule(true);

        assertEquals("TechCorp", experienceData.getCompanyName());
        assertEquals("Software Engineer", experienceData.getJobTitle());
        assertEquals("Remote", experienceData.getJobLocation());
        assertEquals("Developing software", experienceData.getJobDescription());
        assertEquals("2020-01-01", experienceData.getStartDate());
        assertEquals("2022-01-01", experienceData.getEndDate());
        assertTrue(experienceData.isCurrentRule());
    }

    @Test
    void testSpecialFieldDTO() {
        ApplicationResponseDTO.SpecialFieldDTO specialField = new ApplicationResponseDTO.SpecialFieldDTO();
        specialField.setFieldName("Special Field");
        specialField.setData("Special Data");

        assertEquals("Special Field", specialField.getFieldName());
        assertEquals("Special Data", specialField.getData());
    }

    @Test
    void testSpecialSectionDTO() {
        ApplicationResponseDTO.SpecialSectionDTO specialSection = new ApplicationResponseDTO.SpecialSectionDTO();
        specialSection.setSectionName("Special Section");
        specialSection.setData(Map.of("Field1", "Data1", "Field2", "Data2"));

        assertEquals("Special Section", specialSection.getSectionName());
        assertEquals(2, specialSection.getData().size());
        assertEquals("Data1", specialSection.getData().get("Field1"));
        assertEquals("Data2", specialSection.getData().get("Field2"));
    }

    @Test
    void testApplicationResponseDTO() {
        ApplicationResponseDTO dto = new ApplicationResponseDTO();

        // Set nested DTOs
        ApplicationResponseDTO.PersonalDataDTO personalData = new ApplicationResponseDTO.PersonalDataDTO();
        personalData.setFullName("John Doe");

        ApplicationResponseDTO.EducationDataDTO educationData = new ApplicationResponseDTO.EducationDataDTO();
        educationData.setFieldOfStudy("Computer Science");

        ApplicationResponseDTO.ExperienceDataDTO experienceData = new ApplicationResponseDTO.ExperienceDataDTO();
        experienceData.setCompanyName("TechCorp");

        ApplicationResponseDTO.SpecialFieldDTO specialField = new ApplicationResponseDTO.SpecialFieldDTO();
        specialField.setFieldName("Special Field");

        ApplicationResponseDTO.SpecialSectionDTO specialSection = new ApplicationResponseDTO.SpecialSectionDTO();
        specialSection.setSectionName("Special Section");

        // Set lists and attributes
        dto.setPersonalData(personalData);
        dto.setEducationData(educationData);
        dto.setExperienceData(experienceData);
        dto.setSkillData(List.of("Skill1", "Skill2"));
        dto.setSpecialFieldsData(List.of(specialField));
        dto.setSpecialSectionsData(List.of(specialSection));
        dto.setApplicationStatus(1);
        dto.setApplicationId(1001);

        // Assertions
        assertNotNull(dto.getPersonalData());
        assertEquals("John Doe", dto.getPersonalData().getFullName());
        assertNotNull(dto.getEducationData());
        assertEquals("Computer Science", dto.getEducationData().getFieldOfStudy());
        assertNotNull(dto.getExperienceData());
        assertEquals("TechCorp", dto.getExperienceData().getCompanyName());
        assertEquals(2, dto.getSkillData().size());
        assertEquals(1, dto.getSpecialFieldsData().size());
        assertEquals(1, dto.getSpecialSectionsData().size());
        assertEquals(1, dto.getApplicationStatus());
        assertEquals(1001, dto.getApplicationId());
    }
    @Test
    void testMapToSpecialSection_NewSection() {
        List<ApplicationResponseDTO.SpecialSectionDTO> specialSections = new ArrayList<>();

        String sectionName = "Section1";
        String fieldName = "Field1";
        String responseData = "Data1";

        jobService.mapToSpecialSection(specialSections, sectionName, fieldName, responseData);

        assertEquals(1, specialSections.size());
        ApplicationResponseDTO.SpecialSectionDTO sectionDTO = specialSections.get(0);
        assertEquals(sectionName, sectionDTO.getSectionName());
        assertEquals(1, sectionDTO.getData().size());
        assertEquals(responseData, sectionDTO.getData().get(fieldName));
    }

    @Test
    void testMapToSpecialSection_ExistingSection() {
        ApplicationResponseDTO.SpecialSectionDTO existingSection = new ApplicationResponseDTO.SpecialSectionDTO();
        existingSection.setSectionName("Section1");
        existingSection.setData(new HashMap<>(Map.of("Field1", "Data1")));

        List<ApplicationResponseDTO.SpecialSectionDTO> specialSections = new ArrayList<>(List.of(existingSection));

        String sectionName = "Section1";
        String fieldName = "Field2";
        String responseData = "Data2";

        jobService.mapToSpecialSection(specialSections, sectionName, fieldName, responseData);

        assertEquals(1, specialSections.size());
        ApplicationResponseDTO.SpecialSectionDTO sectionDTO = specialSections.get(0);
        assertEquals(sectionName, sectionDTO.getSectionName());
        assertEquals(2, sectionDTO.getData().size());
        assertEquals("Data1", sectionDTO.getData().get("Field1"));
        assertEquals(responseData, sectionDTO.getData().get(fieldName));
    }
}
