package com.software.backend.service;

import com.google.api.client.util.store.AbstractMemoryDataStore;
import com.software.backend.dto.*;
import com.software.backend.enums.ApplicationStatus;
import com.software.backend.enums.JobReportReason;
import com.software.backend.mapper.JobApplicationMapper;
import com.software.backend.repository.*;
import com.software.backend.sorting.SortingContext;
import com.software.backend.entity.Job;
import com.software.backend.enums.JobStatus;
import com.software.backend.filter.JobCriteriaRunner;
import com.software.backend.entity.*;
import com.software.backend.mapper.FieldMapper;
import com.software.backend.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JobService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ReportedJobRepository reportedJobRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private JobCriteriaRunner jobCriteriaRunner;

    @Autowired
    private StaticSectionService staticSectionService;
    @Autowired
    private FieldMapper fieldMapper;
    @Autowired
    private EmailService emailService;
  
    @Autowired
    private SavedJobRepository savedJobRepository;

    @Autowired
    private JobApplicationMapper JobApplicationMapper;

    public List<JobDto> getHomeActiveJobs(int page, int offset){

        Pageable pageable = PageRequest.of(page, offset);

        JobStatus status = JobStatus.OPEN;
        List<Job> jobs = jobRepository.findAllByStatusIs(status, pageable).orElse(new ArrayList<>());

        return jobs.stream().map(jobMapper::jobToJobDto).collect(Collectors.toList());
    }

    public Integer createJobWithCustomForm(String companyUsername, JobDto jobDto) {
        try {
            User user = userRepository.findByUsername(companyUsername).orElse(null);
            if (user == null) throw new IllegalArgumentException("User not found for username: " + companyUsername);

            Company company = user.getCompany();
            if (company == null) throw new IllegalArgumentException("Company not found for user: " + companyUsername);

            Job job = jobMapper.jobDtoToJob(jobDto);
            job.setCompany(company);

            List<Section> sections = getSections(jobDto, job);
            List<Section> staticSections = getStaticSections(jobDto, job);
            sections.addAll(staticSections);
            job.setSections(sections);

            List<Field> fields = getFields(jobDto, job);
            job.setFields(fields);

            jobRepository.save(job);
            return job.getId();
        } catch (Exception e) {
            System.out.println("### Error : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    public HomeDto handleHomeJobs(String username, String type, String location, String category,
                                  String salary, String level, String query,
                                  String sort, int page, int offset) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Applicant not found with username: " + username));
        int applicantId = user.getId();

        HomeDto filteredJobs = filterJobs(type, location, category, salary, level, query, sort, page, offset);
        List<Integer> savedJobsIds = getSavedJobs(applicantId);
        List<Integer> jobIds = filteredJobs.getJobs().stream().map(JobDto::getId).toList();
        List<Integer> appliedJobsIds = jobApplicationRepository.getJobIdByApplicantIdAndJobIds(applicantId, jobIds)
                .orElse(new ArrayList<>());

        for (JobDto job:filteredJobs.getJobs()) {

            job.setSaved(savedJobsIds.contains(job.getId()));

            job.setApplied(appliedJobsIds.contains(job.getId()));
        }
        return filteredJobs;
    }

    public HomeDto filterJobs(String type, String location, String category,
                                   String salary, String level, String query,
                                   String sort, int page, int offset
                                   ){

        HashMap<String, String> filterCriteria = new HashMap<>();

        if (type != null && !type.isEmpty()) filterCriteria.put("employmentType", type);

        if (location != null && !location.isEmpty()) filterCriteria.put("workLocation", location);

        if (category != null) filterCriteria.put("category", category);

        if (salary != null) filterCriteria.put("salary", salary);

        if (level != null && !level.isEmpty()) filterCriteria.put("level", level);

        if (query != null) filterCriteria.put("search", query);


        List<JobDto> jobs = jobCriteriaRunner.matchCriteria(filterCriteria);

        if (sort != null && !sort.isEmpty()) {
            SortingContext sortingContext = new SortingContext(sort);
            jobs = sortingContext.sortJobs(jobs);

        }
        HomeDto homeDto = new HomeDto();
        homeDto.setTotalJobs(jobs.size());
        List<JobDto> paginatedJobs = jobs.stream().skip((long) page * offset).limit(offset).toList();
        homeDto.setJobs(paginatedJobs);
        return homeDto;
    }

    private List<Field> getFields(JobDto jobDto, Job job) {
        List<Field> fields = new ArrayList<>();
        for (FieldDto fieldDto : jobDto.getFields()) {
            Field field = fieldMapper.fieldDtoToField(fieldDto);
            field.setJob(job);
            fields.add(field);
        }
        return fields;
    }

    private List<Section> getStaticSections(JobDto jobDto, Job job) {
        List<Section> staticSections = new ArrayList<>();
        for (String sectionName : jobDto.getStaticSections()) {
            Section section = staticSectionService.getSection(sectionName);
            if (section != null) {
                List<Field> fields = new ArrayList<>();
                for (Field field : section.getFields()) {
                    field.setSection(section);
                    field.setJob(job);
                    fields.add(field);
                }
                section.setFields(fields);
                section.setJob(job);
                staticSections.add(section);
            }
        }
        return staticSections;
    }

    private List<Section> getSections(JobDto jobDto, Job job) {
        List<Section> sections = new ArrayList<>();
        for(SectionDto sectionDto : jobDto.getSections()) {
            Section section = new Section();
            section.setName(sectionDto.getName());
            section.setJob(job);
            List<Field> fields = new ArrayList<>();
            for (int i = 0; i < sectionDto.getLabel().size(); i++) {
                Field field = new Field();
                field.setIsRequired(sectionDto.getIsRequired().get(i));
                field.setLabel(sectionDto.getLabel().get(i));
                field.setType(sectionDto.getType().get(i));
                field.setOptions(sectionDto.getOptions().get(i));
                field.setSection(section);
                field.setJob(job);
                fields.add(field);
            }
            section.setFields(fields);
            sections.add(section);
        }
        return sections;
    }

    public FormDTO getJobForm(int jobId) {
        List<SectionDto> sectionsDTO = new ArrayList<>();
        List<FieldDto> fieldsDTO = new ArrayList<>();
        List<String> staticSections = new ArrayList<>();

        List<Section> sections = sectionRepository.findAllByJobId(jobId);
        addSections(sections, staticSections, sectionsDTO);

        List<Field> fields = fieldRepository.findAllByJobId(jobId);
        addFields(fields, fieldsDTO);

        FormDTO result = new FormDTO();
        result.setSections(sectionsDTO);
        result.setFields(fieldsDTO);
        result.setStaticSections(staticSections);

        return result;
    }

     static void addFields(List<Field> fields, List<FieldDto> fieldsDTO) {
        for (Field field : fields) {
            try {
                int i = field.getSection().getId();
            } catch (NullPointerException e) {
                FieldDto fDto = new FieldDto();
                fDto.setLabel(field.getLabel());
                fDto.setType(field.getType());
                fDto.setOptions(field.getOptions());
                fDto.setIsRequired(field.getIsRequired());

                fieldsDTO.add(fDto);
            }
        }
    }

     void addSections(List<Section> sections, List<String> staticSections, List<SectionDto> sectionsDTO) {
        for (Section section : sections) {
            String name = section.getName();
            if (name.equalsIgnoreCase("Personal Information")
                    || name.equalsIgnoreCase("Education")
                    || name.equalsIgnoreCase("Experience")
                    || name.equalsIgnoreCase("Skills")) {
                staticSections.add(name);
                continue;
            }
            SectionDto secDTO = new SectionDto();
            secDTO.setName(name);
            List<String> labels = new ArrayList<>();
            List<String> types = new ArrayList<>();
            List<List<String>> options = new ArrayList<>();
            List<Boolean> isRequired = new ArrayList<>();

            List<Field> fields = fieldRepository.findAllBySectionId(section.getId());
            for (Field field : fields) {

                labels.add(field.getLabel());
                types.add(field.getType());
                options.add(field.getOptions());
                isRequired.add(field.getIsRequired());
            }
            secDTO.setLabel(labels);
            secDTO.setType(types);
            secDTO.setOptions(options);
            secDTO.setIsRequired(isRequired);

            sectionsDTO.add(secDTO);
        }
    }

    public void submitJobForm(String userName, int jobId, ApplicationResponseDTO dto) {

        JobApplication jobApplication = new JobApplication();

        jobApplication.setJob(jobRepository.findById(jobId).orElse(null));
        jobApplication.setApplicant(applicantRepository.findById(userRepository.findByUsername(userName)
                .orElse(null).getId()).orElse(null));
        jobApplication.setApplicationDate(LocalDateTime.now());
        jobApplication.setApplicationStatus(ApplicationStatus.valueOf("PENDING"));

        List<ApplicationResponse> responses = new ArrayList<>();
        List<Section> sections = sectionRepository.findAllByJobId(jobId);
        Section personalData = null;
        Section educationData = null;
        Section experienceData = null;
        Section skillData = null;

        for (Section section : sections) {
            if (section.getName().equalsIgnoreCase("Personal Information")) {
                personalData = section;
            } else if (section.getName().equalsIgnoreCase("Education")) {
                educationData = section;
            } else if (section.getName().equalsIgnoreCase("Experience")) {
                experienceData = section;
            } else if (section.getName().equalsIgnoreCase("Skills")) {
                skillData = section;
            }

        }

        if (dto.getPersonalData() != null) {
            getPersonalSectionResponses(dto, personalData, jobApplication, responses);
        }
        if (dto.getEducationData() != null) {
            getEducationSectionResponses(dto, educationData, jobApplication, responses);
        }
        if (dto.getExperienceData() != null) {
            getExperienceSectionResponses(dto, experienceData, jobApplication, responses);
        }

        if (!dto.getSkillData().isEmpty()) {
            ApplicationResponse response = new ApplicationResponse();
            response.setField(fieldRepository.findBySectionIdAndLabel(skillData.getId(), "Skill"));
            response.setSection(skillData);
            response.setJobApplication(jobApplication);
            response.setResponseData(dto.getSkillData().toString());
            responses.add(response);
            System.out.println("Skills Done");
        }


        List<Field> fields = fieldRepository.findAllByJobId(jobId);
        getSpecialFieldsResponses(dto, fields, jobApplication, responses);

        getSpecialSectionsResponses(jobId, dto, jobApplication, responses);

        jobApplication.setApplicationResponsesList(responses);
        jobApplicationRepository.save(jobApplication);
    }



    private void getSpecialSectionsResponses(int jobId, ApplicationResponseDTO dto, JobApplication jobApplication, List<ApplicationResponse> responses) {
        for (ApplicationResponseDTO.SpecialSectionDTO specialSectionDTO : dto.getSpecialSectionsData()) {
            Section section = sectionRepository.findByJobIdAndName(jobId, specialSectionDTO.getSectionName());
            for (String key : specialSectionDTO.getData().keySet()) {
                ApplicationResponse response = new ApplicationResponse();
                Field field = fieldRepository.findBySectionIdAndLabel(section.getId(), key);
                response.setField(field);
                response.setSection(section);
                response.setJobApplication(jobApplication);
                response.setResponseData(specialSectionDTO.getData().get(key));
                responses.add(response);
            }
        }
        System.out.println("Special Sections Done");
    }

    private static void getSpecialFieldsResponses(ApplicationResponseDTO dto, List<Field> fields, JobApplication jobApplication, List<ApplicationResponse> responses) {
        for (ApplicationResponseDTO.SpecialFieldDTO specialFieldDTO : dto.getSpecialFieldsData()) {
            ApplicationResponse response = new ApplicationResponse();
            Field field = fields.stream().filter(f -> f.getLabel().equalsIgnoreCase(specialFieldDTO.getFieldName()))
                    .findFirst().orElse(null);

            response.setField(field);
            response.setJobApplication(jobApplication);
            if(!field.getType().equals("checkbox")) {
                response.setResponseData(specialFieldDTO.getData());
            }
            else {
                response.setResponseData(specialFieldDTO.getData().toString());
            }
            responses.add(response);
        }
        System.out.println("Special Fields Done");
    }

    private void processSectionResponses(
            ApplicationResponseDTO dto,
            Section section,
            JobApplication jobApplication,
            List<ApplicationResponse> responses,
            Map<String, String> fieldDataMap) {

        fieldDataMap.forEach((label, responseData) -> {
            Field field = fieldRepository.findBySectionIdAndLabel(section.getId(), label);
            if (field != null) {
                addResponse(responses, section, jobApplication, field, responseData);
            } else {
                System.out.println(label + " Field is null");
            }
        });

        System.out.println(section.getName() + " Done");
    }

    private void addResponse(
            List<ApplicationResponse> responses,
            Section section,
            JobApplication jobApplication,
            Field field,
            String responseData) {

        ApplicationResponse response = new ApplicationResponse();
        response.setField(field);
        response.setSection(section);
        response.setJobApplication(jobApplication);
        response.setResponseData(responseData);
        responses.add(response);
    }


    private void getExperienceSectionResponses(
            ApplicationResponseDTO dto,
            Section experienceData,
            JobApplication jobApplication,
            List<ApplicationResponse> responses) {

        ApplicationResponseDTO.ExperienceDataDTO experience = dto.getExperienceData();

        Map<String, String> fieldDataMap = new LinkedHashMap<>();
        fieldDataMap.put("Company Name", experience.getCompanyName());
        fieldDataMap.put("Job Title", experience.getJobTitle());
        fieldDataMap.put("Location", experience.getJobLocation());
        fieldDataMap.put("Job Description", experience.getJobDescription());
        fieldDataMap.put("Start Date", experience.getStartDate());
        if (!experience.isCurrentRule()) {
            fieldDataMap.put("End Date", experience.getEndDate());
        }
        fieldDataMap.put("Current Role", String.valueOf(experience.isCurrentRule()));

        processSectionResponses(dto, experienceData, jobApplication, responses, fieldDataMap);
    }

    private void getEducationSectionResponses(
            ApplicationResponseDTO dto,
            Section educationData,
            JobApplication jobApplication,
            List<ApplicationResponse> responses) {

        ApplicationResponseDTO.EducationDataDTO education = dto.getEducationData();

        Map<String, String> fieldDataMap = Map.of(
                "Field of Study", education.getFieldOfStudy(),
                "End Date", education.getGraduationYear(),
                "Degree", education.getHighestDegree(),
                "Start Date", education.getStartYear(),
                "University/Institution Name", education.getUniversity()
        );

        processSectionResponses(dto, educationData, jobApplication, responses, fieldDataMap);
    }


    private void getPersonalSectionResponses(
            ApplicationResponseDTO dto,
            Section personalData,
            JobApplication jobApplication,
            List<ApplicationResponse> responses) {

        ApplicationResponseDTO.PersonalDataDTO personal = dto.getPersonalData();

        Map<String, String> fieldDataMap = Map.of(
                "Full Name", personal.getFullName(),
                "Address", personal.getAddress(),
                "Phone Number", personal.getPhoneNumber(),
                "Personal Email", personal.getPersonalEmail(),
                "Portfolio", personal.getPortfolioURL(),
                "LinkedIn URL", personal.getLinkedInURL(),
                "Date of Birth", personal.getDateOfBirth()
        );

        processSectionResponses(dto, personalData, jobApplication, responses, fieldDataMap);
    }

    public void reportJob(int jobId, String userName, JobReportDTO jobReportDTO) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            throw new IllegalArgumentException("Job not found for id: " + jobId);
        }

        User user = userRepository.findByUsername(userName).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found for username: " + userName);
        }

        Applicant applicant = applicantRepository.findById(user.getId()).orElse(null);
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant not found for user: " + userName);
        }

        JobReportReason reason;
        try {
            reason = JobReportReason.valueOf(jobReportDTO.getReason());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid job report reason: " + jobReportDTO.getReason());
        }

        ReportedJob reportedJob = new ReportedJob();
        reportedJob.setJob(job);
        reportedJob.setApplicant(applicant);
        reportedJob.setJobReportReason(reason);
        reportedJob.setReportDescription(jobReportDTO.getDescription());

        reportedJobRepository.save(reportedJob);
    }

    public List<ApplicationResponseDTO> getJobApplications(Integer jobId) {
        List<JobApplication> jobApplications = jobApplicationRepository.findAllByJobId(jobId);
        List<ApplicationResponseDTO> responseDTOs = new ArrayList<>();

        for (JobApplication jobApplication : jobApplications) {
            ApplicationResponseDTO dto = new ApplicationResponseDTO();
            dto.setApplicationStatus(jobApplication.getApplicationStatus().ordinal());
            dto.setApplicationId(jobApplication.getId());
            List<ApplicationResponse> responses = jobApplication.getApplicationResponsesList();

            ApplicationResponseDTO.PersonalDataDTO personalData = new ApplicationResponseDTO.PersonalDataDTO();
            ApplicationResponseDTO.EducationDataDTO educationData = new ApplicationResponseDTO.EducationDataDTO();
            ApplicationResponseDTO.ExperienceDataDTO experienceData = new ApplicationResponseDTO.ExperienceDataDTO();
            List<String> skills = new ArrayList<>();

            List<ApplicationResponseDTO.SpecialFieldDTO> specialFields = new ArrayList<>();
            List<ApplicationResponseDTO.SpecialSectionDTO> specialSections = new ArrayList<>();

            for (ApplicationResponse response : responses) {
                if (response.getSection() != null) {
                    String sectionName = response.getSection().getName();
                    String fieldName = response.getField().getLabel();
                    String responseData = response.getResponseData();

                    // Map to one of the static sections
                    switch (sectionName) {
                        case "Personal Information":
                            mapToPersonalData(personalData, fieldName, responseData);
                            break;
                        case "Education":
                            mapToEducationData(educationData, fieldName, responseData);
                            break;
                        case "Experience":
                            mapToExperienceData(experienceData, fieldName, responseData);
                            break;
                        case "Skills":
                            skills.add(responseData);
                            break;
                        default:
                            // Map Section data to SpecialSectionDTO
                            mapToSpecialSection(specialSections, sectionName, fieldName, responseData);
                            break;
                    }
                } else {
                    // Map Field data to SpecialFieldDTO
                    ApplicationResponseDTO.SpecialFieldDTO specialFieldDTO = new ApplicationResponseDTO.SpecialFieldDTO();
                    specialFieldDTO.setFieldName(response.getField().getLabel());
                    specialFieldDTO.setData(response.getResponseData());
                    specialFields.add(specialFieldDTO);
                }
            }

            // Set data in the DTO
            dto.setPersonalData(personalData);
            dto.setEducationData(educationData);
            dto.setExperienceData(experienceData);
            dto.setSkillData(skills);
            dto.setSpecialFieldsData(specialFields);
            dto.setSpecialSectionsData(specialSections);

            responseDTOs.add(dto);
        }
        for (ApplicationResponseDTO dto : responseDTOs) {
            System.out.println(dto);
        }

        return responseDTOs;
    }

    private void mapToPersonalData(ApplicationResponseDTO.PersonalDataDTO personalData, String fieldName, String responseData) {
        switch (fieldName) {
            case "Full Name":
                personalData.setFullName(responseData);
                break;
            case "Address":
                personalData.setAddress(responseData);
                break;
            case "Phone Number":
                personalData.setPhoneNumber(responseData);
                break;
            case "Personal Email":
                personalData.setPersonalEmail(responseData);
                break;
            case "Portfolio URL":
                personalData.setPortfolioURL(responseData);
                break;
            case "LinkedIn URL":
                personalData.setLinkedInURL(responseData);
                break;
            case "Date of Birth":
                personalData.setDateOfBirth(responseData);
                break;
        }
    }

    private void mapToEducationData(ApplicationResponseDTO.EducationDataDTO educationData, String fieldName, String responseData) {
        switch (fieldName) {
            case "Field of Study":
                educationData.setFieldOfStudy(responseData);
                break;
            case "Graduation Year":
                educationData.setGraduationYear(responseData);
                break;
            case "Highest Degree":
                educationData.setHighestDegree(responseData);
                break;
            case "Start Year":
                educationData.setStartYear(responseData);
                break;
            case "University":
                educationData.setUniversity(responseData);
                break;
        }
    }

    private void mapToExperienceData(ApplicationResponseDTO.ExperienceDataDTO experienceData, String fieldName, String responseData) {
        switch (fieldName) {
            case "Company Name":
                experienceData.setCompanyName(responseData);
                break;
            case "Job Title":
                experienceData.setJobTitle(responseData);
                break;
            case "Job Location":
                experienceData.setJobLocation(responseData);
                break;
            case "Job Description":
                experienceData.setJobDescription(responseData);
                break;
            case "Start Date":
                experienceData.setStartDate(responseData);
                break;
            case "End Date":
                experienceData.setEndDate(responseData);
                break;
            case "Current Role":
                experienceData.setCurrentRule(Boolean.parseBoolean(responseData));
                break;
        }
    }

    public  void mapToSpecialSection(List<ApplicationResponseDTO.SpecialSectionDTO> specialSections, String sectionName, String fieldName, String responseData) {
        boolean sectionExists = false;
        for (ApplicationResponseDTO.SpecialSectionDTO sectionDTO : specialSections) {
            if (sectionDTO.getSectionName().equals(sectionName)) {
                sectionDTO.getData().put(fieldName, responseData);
                sectionExists = true;
                break;
            }
        }
        if (!sectionExists) {
            ApplicationResponseDTO.SpecialSectionDTO specialSectionDTO = new ApplicationResponseDTO.SpecialSectionDTO();
            specialSectionDTO.setSectionName(sectionName);
            Map<String, String> sectionData = new HashMap<>();
            sectionData.put(fieldName, responseData);
            specialSectionDTO.setData(sectionData);
            specialSections.add(specialSectionDTO);
        }
    }

    public void acceptApplication(Integer applicationId) {
        System.out.println("Accepting application...");
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found for id: " + applicationId));

        System.out.println("Application found.");
        System.out.println("application id" + jobApplication.getId());
        System.out.println("jobApllication status: " + jobApplication.getApplicationStatus());

        jobApplication.setApplicationStatus(ApplicationStatus.valueOf("ACCEPTED")); // Enum value should match defined constants
        jobApplicationRepository.save(jobApplication);
        System.out.println("jobApllication status: " + jobApplication.getApplicationStatus());
        emailService.sendApplicationAcceptanceEmail(
                jobApplication.getApplicant().getUser().getEmail(),
                jobApplication.getJob().getTitle(),
                jobApplication.getJob().getCompany().getName()
        );
        System.out.println("Acceptance email sent.");
    }

    public void rejectApplication(Integer applicationId) {
        System.out.println("Rejecting application...");
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found for id: " + applicationId));
        System.out.println("Application found.");
        System.out.println("application id" + jobApplication.getId());
        System.out.println("jobApllication status: " + jobApplication.getApplicationStatus());
        System.out.println(ApplicationStatus.REJECTED);
        jobApplication.setApplicationStatus(ApplicationStatus.valueOf("REJECTED")); // Enum value should match defined constants
        jobApplicationRepository.save(jobApplication);
        System.out.println("jobApplication status: " + jobApplication.getApplicationStatus());
        emailService.sendApplicationRejectionEmail(
                jobApplication.getApplicant().getUser().getEmail(),
                jobApplication.getJob().getTitle(),
                jobApplication.getJob().getCompany().getName()
        );
        System.out.println("Acceptance email sent.");
    }



    private List<Integer> getSavedJobs (int applicantId) {
        return savedJobRepository.getJobIdByApplicantId(applicantId).orElse(new ArrayList<>());
    }


    public void saveJob(String username, int jobId) {
        Applicant applicant = applicantRepository.findByUser_username(username)
                .orElseThrow(() -> new RuntimeException("Applicant not found with username: " + username));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        SavedJob savedJob = new SavedJob();
        savedJob.setApplicant(applicant);
        savedJob.setJob(job);
        savedJob.setCreatedAt(LocalDateTime.now());

        if (savedJobRepository.existsByApplicantIdAndJobId(applicant.getId(), jobId)) {
            throw new RuntimeException("Job already saved");
        }
        try {
            savedJobRepository.save(savedJob);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public void unSaveJob(String username, int jobId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Applicant not found with username: " + username));
        int applicantId = user.getId();
        try {
            savedJobRepository.deleteByApplicantIdAndJobId(applicantId, jobId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<ApplicantApplicationsResponseDto> getApplicationsByApplicant(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Applicant not found with username: " + username));
        int applicantId = user.getId();
        List<JobApplication> applications = jobApplicationRepository.findApplicationsByApplicantId(applicantId).orElse(new ArrayList<>());

        return applications.stream().map(application -> {
            ApplicantApplicationsResponseDto response = JobApplicationMapper.toApplicantApplicationsResponseDto(application);

            response.setResponses(application.getApplicationResponsesList().stream()
                    .map(JobApplicationMapper::toBriefApplicationResponseDto)
                    .collect(Collectors.toList()));

            return response;
        }).collect(Collectors.toList());
    }


}