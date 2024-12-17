package com.software.backend.dto;

import com.software.backend.entity.Company;
import com.software.backend.entity.Field;
import com.software.backend.entity.Section;
import com.software.backend.enums.JobLevel;
import com.software.backend.enums.JobStatus;
import com.software.backend.enums.JobType;
import com.software.backend.enums.PositionType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobDto {
//    private Integer id;
    private String title;
    private String description;
    private String category;
    private String location;
    private Company company;
//    private LocalDateTime postedAt;
    private LocalDateTime applicationDeadline;

    private String salary;
    private JobType type;
    private JobLevel level;
    private PositionType positionType;
    private List<SectionDto> sections; // sections with fields
    private List<String> staticSections; // static sections
    private List<FieldDto> fields;     // standalone fields

    // private JobStatus status; // to be discussed
}
