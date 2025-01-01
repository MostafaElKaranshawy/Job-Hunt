package com.software.backend.service;

import com.software.backend.dto.*;
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
import java.util.stream.Collectors;

@Service
public class JobService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private JobCriteriaRunner jobCriteriaRunner;

    @Autowired
    private StaticSectionService staticSectionService;
    @Autowired
    private FieldMapper fieldMapper;

    @Autowired
    private SavedJobRepository savedJobRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private JobApplicationRepository jobApplicationRepository;

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


        List<JobDto> jobs =  jobCriteriaRunner.matchCriteria(filterCriteria);

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
                    .map(JobApplicationMapper::toApplicationResponseDTO)
                    .collect(Collectors.toList()));

            return response;
        }).collect(Collectors.toList());
    }


}