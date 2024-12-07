package com.software.backend.repository;

import com.software.backend.entity.Applicant;
import com.software.backend.entity.Education;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationRepository extends JpaRepository<Education, Integer> {
    List<Education> findByApplicant(Applicant applicant);
    Optional<Education> findById(Integer id);
}