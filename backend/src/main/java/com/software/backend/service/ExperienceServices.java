package com.software.backend.service;

import com.software.backend.dto.ExperienceDTO;
import com.software.backend.entity.Applicant;
import com.software.backend.entity.Experience;
import com.software.backend.mapper.ExperienceMapper;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.ExperienceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienceServices {
    @Autowired
    ExperienceRepository repo;
    @Autowired
    ApplicantRepository ApplicantRepo;
    @Autowired
    ExperienceMapper mapper;

    public boolean addExperience(Integer applicantId, ExperienceDTO dto) {
        Applicant applicant = ApplicantRepo.findById(applicantId).orElse(null);
        if(applicant == null) return false;
        Experience experience = mapper.toEntity(dto);
        experience.setApplicant(applicant);
        repo.save(experience);
        return true;
    }

    public List<ExperienceDTO> getExperience(Integer applicantId) {
        Applicant applicant = ApplicantRepo.findById(applicantId)
                .orElseThrow(() -> new EntityNotFoundException("Applicant not found for id: " + applicantId));

        List<Experience> experienceList = repo.findByApplicant(applicant);

        return experienceList.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean updateExperience(Integer experienceId, ExperienceDTO dto) {
        Experience experienceToUpdate = repo.findById(experienceId).orElse(null);
        if(experienceToUpdate == null) return false;
        mapper.updateEntityFromDTO(dto, experienceToUpdate);
        repo.save(experienceToUpdate);
        return true;
    }

    public boolean deleteExperience(Integer experienceId) {
        Experience experience = repo.findById(experienceId).orElse(null);
        if(experience == null) return false;
        repo.delete(experience);
        return true;
    }
}
