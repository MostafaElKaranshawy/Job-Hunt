package com.software.backend.service;

import com.software.backend.dto.*;
import com.software.backend.enums.ApplicationStatus;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private FieldRepository fieldRepository;
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


    public List<JobDto> getHomeActiveJobs(int page, int offset) {

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

    public HomeDto filterJobs(String type, String location, String category,
                              String salary, String level, String query,
                              String sort, int page, int offset
    ) {

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

    private static List<Section> getSections(JobDto jobDto, Job job) {
        List<Section> sections = new ArrayList<>();
        for (SectionDto sectionDto : jobDto.getSections()) {
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

}

