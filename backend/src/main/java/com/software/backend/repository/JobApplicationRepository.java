package com.software.backend.repository;

import com.software.backend.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {
    List<JobApplication> findAllByJobId(Integer jobId);
    Optional<JobApplication> findById(Integer id);
}
