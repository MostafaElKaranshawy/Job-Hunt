package com.software.backend.repository;

import com.software.backend.entity.Applicant;
import com.software.backend.entity.Experience;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Integer> {
    List<Experience> findByApplicant(Applicant applicant);
}