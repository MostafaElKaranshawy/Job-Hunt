package com.software.backend.repository;

import com.software.backend.entity.Company;
import com.software.backend.entity.Job;

import com.software.backend.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

    Optional<List<Job>> findByCompanyAndApplicationDeadlineBefore(Company company, LocalDateTime currentDateTime);// expired jobs
    Optional<List<Job>> findByCompanyAndApplicationDeadlineAfter(Company company, LocalDateTime currentDateTime); // active jobs
    Optional<List<Job>> findAllByStatusIs(JobStatus status, Pageable pageable);  // find All Active Jobs for Home page

    // Search for title or description.
    Optional<List<Job>> findAllByTitleContainsOrDescriptionContains(String title, String description, Pageable pageable);
    Optional<List<Job>> findAllByTitleContainsOrDescriptionContains(String title, String description);

    // Filter
    Optional<List<Job>> findAllByTypeContainsIgnoreCase(String type);
    Optional<List<Job>> findAllByLocationContainsIgnoreCase(String location);
    Optional<List<Job>> findAllByCategoryContainsIgnoreCase(String category);
    Optional<List<Job>> findAllBySalaryGreaterThanEqual(Integer salary);
    Optional<List<Job>> findAllByLevelContainsIgnoreCase(String level);


}