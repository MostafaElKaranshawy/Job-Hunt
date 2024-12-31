package com.software.backend.repository;

import com.software.backend.entity.ReportedApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportedApplicantRepository extends JpaRepository<ReportedApplicant, Integer> {
}
