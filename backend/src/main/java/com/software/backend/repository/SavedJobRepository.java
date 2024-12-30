package com.software.backend.repository;

import com.software.backend.entity.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Integer> {
    Optional<List<Integer>> getJobIdByApplicantId(Integer applicantId);
    void deleteByApplicantIdAndJobId(Integer applicantId, Integer jobId);
}
