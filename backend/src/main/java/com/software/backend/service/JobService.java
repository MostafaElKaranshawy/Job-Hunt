package com.software.backend.service;

import com.software.backend.dto.*;
import com.software.backend.enums.ApplicationStatus;
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

import java.time.LocalDateTime;
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

    private static void addFields(List<Field> fields, List<FieldDto> fieldsDTO) {
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

    private void addSections(List<Section> sections, List<String> staticSections, List<SectionDto> sectionsDTO) {
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
            ApplicationResponse response = new ApplicationResponse();
            Field field = fieldRepository.findBySectionIdAndLabel(personalData.getId(), "Full Name");
            if (field != null) {
                response.setField(field);
                response.setSection(personalData);
                response.setJobApplication(jobApplication);
                response.setResponseData(dto.getPersonalData().getFullName());
                responses.add(response);
            }else {
                System.out.println("Full name Field is null");
            }

            response = new ApplicationResponse();
            Field field1 = fieldRepository.findBySectionIdAndLabel(personalData.getId(), "Address");
            if(field1 != null) {
                response.setField(field1);
                response.setJobApplication(jobApplication);
                response.setResponseData(dto.getPersonalData().getAddress());
                responses.add(response);
            }else {
                System.out.println("Address Field is null");
            }

            response = new ApplicationResponse();
            Field field2 = fieldRepository.findBySectionIdAndLabel(personalData.getId(), "Phone Number");
            if(field2 != null) {
                response.setField(field2);
                response.setJobApplication(jobApplication);
                response.setResponseData(dto.getPersonalData().getPhoneNumber());
                responses.add(response);
            }else {
                System.out.println("Phone Number Field is null");
            }

            response = new ApplicationResponse();
            Field field3 = fieldRepository.findBySectionIdAndLabel(personalData.getId(), "Personal Email");
            if(field3 != null) {
                response.setField(field3);
                response.setJobApplication(jobApplication);
                response.setResponseData(dto.getPersonalData().getPersonalEmail());
                responses.add(response);
            }else {
                System.out.println("Personal Email Field is null");
            }


            response = new ApplicationResponse();
            Field field4 = fieldRepository.findBySectionIdAndLabel(personalData.getId(), "Portfolio");
            if (field4 != null) {
                response.setField(field4);
                response.setJobApplication(jobApplication);
                response.setResponseData(dto.getPersonalData().getPortfolioURL());
                responses.add(response);
            }else {
                System.out.println("Portfolio URL Field is null");
            }

            response = new ApplicationResponse();
            Field field5 = fieldRepository.findBySectionIdAndLabel(personalData.getId(), "LinkedIn URL");
            if(field5 != null) {
                response.setField(field5);
                response.setJobApplication(jobApplication);
                response.setResponseData(dto.getPersonalData().getLinkedInURL());
                responses.add(response);
            }else {
                System.out.println("LinkedIn URL Field is null");
            }

            response = new ApplicationResponse();
            Field field6 = fieldRepository.findBySectionIdAndLabel(personalData.getId(), "Date of Birth");
            if(field6 != null) {
                response.setField(field6);
                response.setJobApplication(jobApplication);
                response.setResponseData(dto.getPersonalData().getDateOfBirth());
                responses.add(response);
                System.out.println("Personal Done");
            }else {
                System.out.println("Date of Birth Field is null");
            }
        }



        if (dto.getEducationData() != null) {
            ApplicationResponse response = new ApplicationResponse();
            Field field = fieldRepository.findBySectionIdAndLabel(educationData.getId(), "Field of Study");
            if(field != null) {
                System.out.println(dto.getEducationData().getFieldOfStudy());
                response.setField(field);
                response.setJobApplication(jobApplication);
                response.setResponseData(dto.getEducationData().getFieldOfStudy());
                responses.add(response);
            }else {
                System.out.println("Field of Study Field is null");
            }

            response = new ApplicationResponse();
            Field field1 = fieldRepository.findBySectionIdAndLabel(educationData.getId(), "End Date");
            if(field1 != null) {
                System.out.println(dto.getEducationData().getGraduationYear());
                response.setField(field1);
                response.setJobApplication(jobApplication);
                response.setResponseData(dto.getEducationData().getGraduationYear());
                responses.add(response);
            }
            else {
                System.out.println("End Date Field is null");
            }

            response = new ApplicationResponse();
            Field field2 = fieldRepository.findBySectionIdAndLabel(educationData.getId(), "Degree");
            if(field2 != null) {
                System.out.println(dto.getEducationData().getHighestDegree());
                response.setField(field2);
                response.setJobApplication(jobApplication);
                response.setResponseData(dto.getEducationData().getHighestDegree());
                responses.add(response);
            }
            else {
                System.out.println("Degree Field is null");
            }

            response = new ApplicationResponse();
            Field field3 = fieldRepository.findBySectionIdAndLabel(educationData.getId(), "Start Date");
            if(field3 != null) {
                System.out.println(dto.getEducationData().getStartYear());
                response.setField(field3);
                response.setJobApplication(jobApplication);
                response.setResponseData(dto.getEducationData().getStartYear());
                responses.add(response);
            }
            else {
                System.out.println("Start Date Field is null");
            }

            response = new ApplicationResponse();
            Field field4 = fieldRepository.findBySectionIdAndLabel(educationData.getId(), "University/Institution Name");
            if(field4 != null) {
                System.out.println(dto.getEducationData().getUniversity());
                response.setField(field4);
                response.setJobApplication(jobApplication);
                response.setResponseData(dto.getEducationData().getUniversity());
                responses.add(response);
            }
            else {
                System.out.println("University Field is null");
            }
            System.out.println("Education Done");
        }

        if (dto.getExperienceData() != null) {
            ApplicationResponse response = new ApplicationResponse();
            response.setField(fieldRepository.findBySectionIdAndLabel(experienceData.getId(), "Company Name"));
            response.setJobApplication(jobApplication);
            response.setResponseData(dto.getExperienceData().getCompanyName());
            responses.add(response);

            response = new ApplicationResponse();
            response.setField(fieldRepository.findBySectionIdAndLabel(experienceData.getId(), "Job Title"));
            response.setJobApplication(jobApplication);
            response.setResponseData(dto.getExperienceData().getJobTitle());
            responses.add(response);

            response = new ApplicationResponse();
            response.setField(fieldRepository.findBySectionIdAndLabel(experienceData.getId(), "Location"));
            response.setJobApplication(jobApplication);
            response.setResponseData(dto.getExperienceData().getJobLocation());
            responses.add(response);

            response = new ApplicationResponse();
            response.setField(fieldRepository.findBySectionIdAndLabel(experienceData.getId(), "Job Description"));
            response.setJobApplication(jobApplication);
            response.setResponseData(dto.getExperienceData().getJobDescription());
            responses.add(response);

            response = new ApplicationResponse();
            response.setField(fieldRepository.findBySectionIdAndLabel(experienceData.getId(), "Start Date"));
            response.setJobApplication(jobApplication);
            response.setResponseData(dto.getExperienceData().getStartDate());
            responses.add(response);

            if (!dto.getExperienceData().isCurrentRule()) {
                response = new ApplicationResponse();
                response.setField(fieldRepository.findBySectionIdAndLabel(experienceData.getId(), "End Date"));
                response.setJobApplication(jobApplication);
                response.setResponseData(dto.getExperienceData().getEndDate());
                responses.add(response);
            }

            response = new ApplicationResponse();
            response.setField(fieldRepository.findBySectionIdAndLabel(experienceData.getId(), "Current Role"));
            response.setJobApplication(jobApplication);
            response.setResponseData(String.valueOf(dto.getExperienceData().isCurrentRule()));
            responses.add(response);
            System.out.println("Experience Done");
        }

        if (!dto.getSkillData().isEmpty()) {
            ApplicationResponse response = new ApplicationResponse();
            response.setField(fieldRepository.findBySectionIdAndLabel(skillData.getId(), "Skill"));
            response.setJobApplication(jobApplication);
            response.setResponseData(dto.getSkillData().toString());
            responses.add(response);
            System.out.println("Skills Done");
        }


        List<Field> fields = fieldRepository.findAllByJobId(jobId);
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

        for (ApplicationResponseDTO.SpecialSectionDTO specialSectionDTO : dto.getSpecialSectionsData()) {
            Section section = sectionRepository.findByJobIdAndName(jobId, specialSectionDTO.getSectionName());
            for (String key : specialSectionDTO.getData().keySet()) {
                ApplicationResponse response = new ApplicationResponse();
                Field field = fieldRepository.findBySectionIdAndLabel(section.getId(), key);
                response.setField(field);
                response.setJobApplication(jobApplication);
                response.setResponseData(specialSectionDTO.getData().get(key));
                responses.add(response);
            }

        }
        System.out.println("Special Sections Done");

        jobApplication.setApplicationResponsesList(responses);
        jobApplicationRepository.save(jobApplication);
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

    private void mapToSpecialSection(List<ApplicationResponseDTO.SpecialSectionDTO> specialSections, String sectionName, String fieldName, String responseData) {
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