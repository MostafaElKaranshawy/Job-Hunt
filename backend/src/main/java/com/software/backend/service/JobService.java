package com.software.backend.service;

import com.software.backend.dto.JobDto;
import com.software.backend.entity.Company;
import com.software.backend.entity.Job;
import com.software.backend.entity.User;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.JobRepository;
import com.software.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
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

