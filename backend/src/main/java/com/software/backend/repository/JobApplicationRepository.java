package com.software.backend.repository;

import com.software.backend.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {
    @Query("SELECT s.job.id FROM JobApplication s WHERE s.applicant.id = :applicantId AND s.job.id IN :jobIds")
    Optional<List<Integer>> getJobIdByApplicantIdAndJobIds(@Param("applicantId") Integer applicantId, @Param("jobIds") List<Integer> jobIds);

    @Query("SELECT a FROM JobApplication a " +
            "JOIN FETCH a.applicationResponsesList r " +
            "JOIN FETCH r.field f " +
            "WHERE a.applicant.id = :applicantId")
    Optional<List<JobApplication>> findApplicationsByApplicantId(@Param("applicantId") Integer applicantId);
}
