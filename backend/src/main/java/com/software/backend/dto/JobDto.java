package com.software.backend.dto;

import com.software.backend.entity.Company;
import com.software.backend.enums.EmploymentType;
import com.software.backend.enums.JobLevel;
import com.software.backend.enums.WorkLocation;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobDto {
    private String title;
    private String description;
    private String category;
    private String location;
    private Company company;
    private LocalDateTime applicationDeadline;
    private String salary;
    private WorkLocation workLocation;
    private JobLevel level;
    private EmploymentType employmentType;
    private List<SectionDto> sections; // sections with fields
    private List<String> staticSections; // static sections
    private List<FieldDto> fields;     // standalone fields
}
