package com.software.backend.service;

import com.software.backend.dto.EducationDTO;
import com.software.backend.entity.Applicant;
import com.software.backend.entity.Education;
import com.software.backend.mapper.EducationMapper;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.EducationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EducationServices {
    @Autowired
    ApplicantRepository ApplicantRepo;
    @Autowired
    EducationRepository repo;
    @Autowired
    EducationMapper mapper;
    public void addEducation(Integer applicantId, EducationDTO dto) {
        Applicant applicant = ApplicantRepo.findById(applicantId)
                .orElseThrow(() -> new EntityNotFoundException("Applicant not found for id: " + applicantId));
        Education education = mapper.toEntity(dto);
        education.setApplicant(applicant);
        repo.save(education);
    }
    public List<EducationDTO> getEducation(Integer applicantId) {
        Applicant applicant = ApplicantRepo.findById(applicantId)
                .orElseThrow(() -> new EntityNotFoundException("Applicant not found for id: " + applicantId));

        List<Education> educationList = repo.findByApplicant(applicant);

        return educationList.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
