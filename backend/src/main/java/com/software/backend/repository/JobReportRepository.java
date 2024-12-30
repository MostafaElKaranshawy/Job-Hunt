package com.software.backend.repository;


import com.software.backend.entity.JobReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JobReportRepository extends JpaRepository<JobReport, Integer> {
}
