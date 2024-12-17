package com.software.backend.repository;

import com.software.backend.entity.Company;
import com.software.backend.entity.Job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

    Optional<List<Job>> findByCompanyAndApplicationDeadlineBefore(Company company, LocalDateTime currentDateTime);// expired jobs
    Optional<List<Job>> findByCompanyAndApplicationDeadlineAfter(Company company, LocalDateTime currentDateTime); // active jobs
    Optional<Job> findByCompanyAndId(Company company, Integer jobId);
}