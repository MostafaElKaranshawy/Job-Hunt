package com.software.backend.service;

import com.software.backend.dto.CompanyDto;
import com.software.backend.dto.JobDto;
import com.software.backend.entity.Company;
import com.software.backend.entity.Job;
import com.software.backend.entity.User;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.CompanyRepository;
import com.software.backend.mapper.CompanyMapper;
import com.software.backend.repository.JobRepository;
import com.software.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyMapper companyMapper;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    JobMapper jobMapper;

    public CompanyDto getCompanyInfo(String companyUsername) {
        User user = userRepository.findByUsername(companyUsername).orElse(null);
        if (user == null) return null;

        Company company = user.getCompany();
        if (company == null) return null;

        return companyMapper.toCompanyDTO(company);
    }

    public CompanyDto updateCompanyInfo(String companyUsername, CompanyDto newCompanyDto){
        User user = userRepository.findByUsername(companyUsername).orElse(null);
        if (user == null) return null;

        Company company = user.getCompany();
        if (company == null) return null;

        return getCompanyDto(newCompanyDto, company);
    }

    private CompanyDto getCompanyDto(CompanyDto newCompanyDto, Company company) {

        if(newCompanyDto.getName() != null) company.setName(newCompanyDto.getName());
        if(newCompanyDto.getWebsite() != null) company.setWebsite(newCompanyDto.getWebsite());
        if(newCompanyDto.getLocation() != null) company.setLocation(newCompanyDto.getLocation());
        if(newCompanyDto.getOverview() != null) company.setOverview(newCompanyDto.getOverview());

        companyRepository.save(company);
        return companyMapper.toCompanyDTO(company);
    }

    public Map<String, List<JobDto>> getCompanyJobs(String companyUsername) {
        User user = userRepository.findByUsername(companyUsername).orElse(null);
        if (user == null) return null;

        Company company = user.getCompany();
        if (company == null) return null;

        List<Job> expiredJobs = jobRepository.findByCompanyAndApplicationDeadlineBefore(company, LocalDateTime.now()).orElse(null);
        List<Job> activeJobs = jobRepository.findByCompanyAndApplicationDeadlineAfter(company, LocalDateTime.now()).orElse(null);

        if (expiredJobs == null) expiredJobs = new ArrayList<>();
        if (activeJobs == null) activeJobs = new ArrayList<>();
        List<JobDto> expiredJobDtos = new ArrayList<>();
        for (Job expiredJob : expiredJobs) {
            JobDto jobDto = jobMapper.jobToJobDto(expiredJob);
            expiredJobDtos.add(jobDto);
        }

        List<JobDto> activeJobDtos = new ArrayList<>();
        for (Job activeJob : activeJobs) {
            JobDto jobDto = jobMapper.jobToJobDto(activeJob);
            activeJobDtos.add(jobDto);
        }
        Map<String, List<JobDto>> map = Map.of("expired", expiredJobDtos, "active", activeJobDtos);
        return map;
    }

    public boolean deleteJob(String companyUsername, Integer jobId) {
        User user = userRepository.findByUsername(companyUsername).orElse(null);
        if (user == null) return false;

        Company company = user.getCompany();
        if (company == null) return false;

        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) return false;

        if (!Objects.equals(job.getCompany().getId(), company.getId())) return false;

        jobRepository.delete(job);
        return true;
    }
}
