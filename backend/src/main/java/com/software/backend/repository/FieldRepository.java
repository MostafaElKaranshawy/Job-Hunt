package com.software.backend.repository;

import com.software.backend.entity.Field;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<Field, Integer> {
    List<Field> findAllBySectionId(Integer id);

    List<Field> findAllByJobId(int jobId);
}