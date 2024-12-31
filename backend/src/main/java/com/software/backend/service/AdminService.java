package com.software.backend.service;


import com.software.backend.dto.ReportedApplicantDto;
import com.software.backend.dto.ReportedJobDto;
import com.software.backend.entity.ReportedJob;
import com.software.backend.entity.User;
import com.software.backend.exception.BusinessException;
import com.software.backend.exception.UserNotFoundException;
import com.software.backend.mapper.ReportedApplicantMapper;
import com.software.backend.mapper.ReportedJobMapper;
import com.software.backend.repository.JobRepository;
import com.software.backend.repository.ReportedApplicantRepository;
import com.software.backend.repository.ReportedJobRepository;
import com.software.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportedJobRepository reportedJobRepository;

    @Autowired
    private ReportedApplicantRepository reportedApplicantRepository;

    @Autowired
    private ReportedJobMapper reportedJobMapper;

    @Autowired
    private ReportedApplicantMapper reportedApplicantMapper;

    @Autowired
    private JobRepository jobRepository;

    public boolean banUser(Integer applicantId) {

        User user =  userRepository.findById(applicantId).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + applicantId)
        );

        user.setIsBanned(true);
        userRepository.save(user);
        return true;
    }

    public Page<ReportedJob> getReports(Integer page, Integer offset) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, offset, sort);

        reportedJobRepository.findAll(pageable);

        return reportedJobRepository.findAll(pageable);
    }

    public Page<ReportedJobDto> getReportedJobs(Integer page, Integer offset) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, offset, sort);

        return reportedJobRepository.findAll(pageable).map(reportedJobMapper::toDto);
    }

    public Page<ReportedApplicantDto> getReportedApplicants(Integer page, Integer offset) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, offset, sort);

        return reportedApplicantRepository.findAll(pageable).map(reportedApplicantMapper::toDto);
    }

    public boolean deleteReportedJob(Integer jobReportId) {
        if (!reportedJobRepository.existsById(jobReportId))
            throw new BusinessException("Reported job not found with id: " + jobReportId);

        reportedJobRepository.deleteById(jobReportId);
        return true;
    }

    public boolean deleteReportedApplicant(Integer applicantReportId) {
        if (!reportedApplicantRepository.existsById(applicantReportId))
            throw new BusinessException("Reported applicant not found with id: " + applicantReportId);

        reportedApplicantRepository.deleteById(applicantReportId);
        return true;
    }

    public boolean deleteJob(Integer jobId) {
        if (!jobRepository.existsById(jobId))
            throw new BusinessException("Job not found with id: " + jobId);

        jobRepository.deleteById(jobId);
        return true;
    }
}












