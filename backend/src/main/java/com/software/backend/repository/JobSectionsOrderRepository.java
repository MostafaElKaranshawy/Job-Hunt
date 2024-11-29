package com.software.backend.repository;

import com.software.backend.entity.JobSectionsOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSectionsOrderRepository extends JpaRepository<JobSectionsOrder, Integer> {
}