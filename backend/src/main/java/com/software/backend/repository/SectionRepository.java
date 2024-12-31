package com.software.backend.repository;

import com.software.backend.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    List<Section> findAllByJobId(int jobId);

    Section findByJobIdAndName(int jobId, String name);
}
