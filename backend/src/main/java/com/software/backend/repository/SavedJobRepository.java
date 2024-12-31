package com.software.backend.repository;

import com.software.backend.entity.Job;
import com.software.backend.entity.SavedJob;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Integer> {

    @Query("SELECT s.job.id FROM SavedJob s WHERE s.applicant.id = :applicantId")
    Optional<List<Integer>> getJobIdByApplicantId(@Param("applicantId") Integer applicantId);

    Optional<List<SavedJob>> getSavedJobsByApplicantId(Integer applicantId, Pageable pageable);

    @Query("SELECT COUNT(s) FROM SavedJob s WHERE s.applicant.id = :applicantId")
    Integer getSavedJobsCountByApplicantId(@Param("applicantId") Integer applicantId);

    boolean existsByApplicantIdAndJobId(Integer applicantId, Integer jobId);
    void deleteByApplicantIdAndJobId(Integer applicantId, Integer jobId);
}
