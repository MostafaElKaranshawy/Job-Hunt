package com.software.backend.service;

import com.software.backend.dto.JobDto;
import com.software.backend.entity.Company;
import com.software.backend.entity.Job;
import com.software.backend.entity.User;
import com.software.backend.enums.JobStatus;
import com.software.backend.filter.JobCriteriaRunner;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.JobRepository;
import com.software.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    public List<JobDto> getHomeActiveJobs(int page, int offset){

        Pageable pageable = PageRequest.of(page, offset);

        JobStatus status = JobStatus.OPEN;
        List<Job> jobs = jobRepository.findAllByStatusIs(status, pageable).orElse(new ArrayList<>());

        return jobs.stream().map(jobMapper::jobToJobDto).collect(Collectors.toList());
    }
    public List<JobDto> searchJobs(String query, int page, int offset){

        Pageable pageable = PageRequest.of(page, offset);
        List<Job> jobs = jobRepository.findAllByTitleContainsOrDescriptionContains(query, query, pageable).orElse(new ArrayList<>());

        return jobs.stream().map(jobMapper::jobToJobDto).collect(Collectors.toList());
    }

    public List<JobDto> filterJobs(String type, String location, String category, String salary, String level, String query){

        HashMap<String, String> filterCriteria = new HashMap<>();

        if(type != null && !type.isEmpty())filterCriteria.put("type", type);

        if(location != null && !location.isEmpty())filterCriteria.put("location", location);

        if(category != null && !category.isEmpty())filterCriteria.put("category", category);

        if(salary != null && !salary.isEmpty())filterCriteria.put("salary", salary);

        if(level != null && !level.isEmpty())filterCriteria.put("level", level);

        if(query != null && !query.isEmpty())filterCriteria.put("search", query);

        List<JobDto> filteredJobs = jobCriteriaRunner.matchCriterias(filterCriteria);

        if (filteredJobs == null) return new ArrayList<>();
        return filteredJobs;
    }

    public List<JobDto> getExpiredJobsForCompany(String companyUsername) {
        User user = userRepository.findByUsername(companyUsername).orElse(null);

        if (user == null) return Collections.emptyList();

        Company company = user.getCompany();

        if (company == null) return Collections.emptyList();

        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Job> jobs = jobRepository.findByCompanyAndApplicationDeadlineBefore(company, currentDateTime).orElse(null);

        if (jobs == null) return Collections.emptyList();

        return jobs.stream().map(jobMapper::jobToJobDto).collect(Collectors.toList());
    }

    public List<JobDto> getActiveJobsForCompany(String companyUsername) {

        User user = userRepository.findByUsername(companyUsername).orElse(null);

        if (user == null) return Collections.emptyList();

        Company company = user.getCompany();

        if (company == null) return Collections.emptyList();

        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Job> jobs = jobRepository.findByCompanyAndApplicationDeadlineAfter(company, currentDateTime).orElse(null);

        if (jobs == null) return Collections.emptyList();

        return jobs.stream().map(jobMapper::jobToJobDto).collect(Collectors.toList());
    }
}

