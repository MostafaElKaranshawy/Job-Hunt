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
    public void updateApplicant(String username, ApplicantDTO dto) {
            if (userRepo.existsByUsername(username)) {
            User user = userRepo.findByUsername(username).orElse(null);

            Applicant applicant = mapper.ApplicantDTOToApplicant(dto);
            applicant.setUser(user);
            repo.save(applicant);
        } else {
            throw new EntityNotFoundException("Applicant not found for update.");

        }
    }

}
