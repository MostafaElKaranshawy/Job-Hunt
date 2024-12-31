package com.software.backend.service;

import com.software.backend.dto.ApplicantDTO;
import com.software.backend.dto.JobDto;
import com.software.backend.entity.Applicant;
import com.software.backend.entity.Job;
import com.software.backend.entity.SavedJob;
import com.software.backend.entity.User;
import com.software.backend.mapper.ApplicantMapper;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.SavedJobRepository;
import com.software.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ApplicantServices {
    @Autowired
    ApplicantRepository repo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    SavedJobRepository savedJobRepository;
    @Autowired
    ApplicantMapper mapper;
    @Autowired
    JobMapper jobMapper;
    private ApplicantRepository applicantRepository;

    public ApplicantDTO getApplicant(String username) {
        User user = userRepo.findByUsername(username).orElse(null);
        if(user != null){
            Applicant applicant = repo.findById(user.getId()).orElse(null);
            return mapper.applicantToApplicantDTO(applicant);
        }
        return null;
    }
    public boolean updateApplicant(String username, ApplicantDTO dto) {
        User user = userRepo.findByUsername(username).orElse(null);
        if(user != null) {
            Applicant existingApplicant = repo.findById(user.getId()).orElse(null);
            if(existingApplicant != null) {
                mapper.updateApplicantFromDTO(dto, existingApplicant);
                repo.save(existingApplicant);
                return true;
            }
        }
        return false;
    }

    public List<String> getSkills(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        if(user != null){
            Applicant applicant = repo.findById(user.getId()).orElse(null);
            if(applicant != null) {
                List<String> skills = applicant.getSkills();
                if (skills != null) return skills;
                else return new ArrayList<>();
            }
        }
        return null;
    }

    public boolean setSkills(String username, List<String> skills) {
        User user = userRepo.findByUsername(username).orElse(null);
        if(user != null){
            Applicant applicant = repo.findById(user.getId()).orElse(null);
            if(applicant != null) {
                applicant.setSkills(skills);
                repo.save(applicant);
                return true;
            }
        }
        return false;
    }

    public List<JobDto> getSavedJobs(String username, int page, int offset) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        if(user != null){
            Applicant applicant = repo.findById(user.getId()).orElse(null);
            Pageable pageable = PageRequest.of(page, offset);
            if(applicant != null) {
                List<SavedJob> savedJobs = savedJobRepository.getSavedJobsByApplicantId(applicant.getId(), pageable).orElse(new ArrayList<>());
                List<Job> jobs = savedJobs.stream().map(SavedJob::getJob).toList();
                return jobs.stream().map(jobMapper::jobToJobDto).collect(Collectors.toList());
            }
        }
        return null;
    }
}
