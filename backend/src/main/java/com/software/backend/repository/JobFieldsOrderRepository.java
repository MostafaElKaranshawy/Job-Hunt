package com.software.backend.repository;

import com.software.backend.entity.JobFieldsOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobFieldsOrderRepository extends JpaRepository<JobFieldsOrder, Integer> {
}