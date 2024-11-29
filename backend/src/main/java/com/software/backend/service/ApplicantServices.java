package com.software.backend.service;

import com.software.backend.entity.Applicant;
import com.software.backend.repository.ApplicantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicantServices {
    @Autowired
    ApplicantRepository repo;
    public Applicant getApplicant(int id) {
        return repo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Applicant with ID " + id + " not found.")
        );
    }


    public void updateApplicant(Applicant applicant) {
        if (repo.existsById(applicant.getId())) {
            repo.save(applicant);
        } else {
            throw new EntityNotFoundException("Applicant not found for update.");
        }
    }

}
