package com.software.backend.service;

import com.software.backend.dto.HomeDto;
import com.software.backend.sorting.SortingContext;
import com.software.backend.dto.JobDto;
import com.software.backend.entity.Job;
import com.software.backend.enums.JobStatus;
import com.software.backend.filter.JobCriteriaRunner;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.JobRepository;
import com.software.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

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

    public List<JobDto> getHomeActiveJobs(int page, int offset){

        Pageable pageable = PageRequest.of(page, offset);

        JobStatus status = JobStatus.OPEN;
        List<Job> jobs = jobRepository.findAllByStatusIs(status, pageable).orElse(new ArrayList<>());

        return jobs.stream().map(jobMapper::jobToJobDto).collect(Collectors.toList());
    }
    public List<JobDto> searchJobs(String query, int page, int offset){

        Pageable pageable = PageRequest.of(page, offset);
        List<Job> jobs = jobRepository.findAllByTitleContainsOrCompany_NameContainsIgnoreCase(query, query).orElse(new ArrayList<>());

        return jobs.stream().map(jobMapper::jobToJobDto).collect(Collectors.toList());
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
}

