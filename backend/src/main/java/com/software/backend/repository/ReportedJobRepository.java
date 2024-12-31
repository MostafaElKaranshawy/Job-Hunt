package com.software.backend.repository;

import com.software.backend.entity.ReportedJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportedJobRepository extends JpaRepository<ReportedJob, Integer> {
}
