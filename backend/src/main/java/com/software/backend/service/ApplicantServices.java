package com.software.backend.service;

import com.software.backend.dto.ApplicantDTO;
import com.software.backend.entity.Applicant;
import com.software.backend.entity.User;
import com.software.backend.mapper.ApplicantMapper;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ApplicantServices {
    @Autowired
    ApplicantRepository repo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    ApplicantMapper mapper;
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
}
