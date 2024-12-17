package com.software.backend.dto;

import com.software.backend.enums.EmploymentType;
import com.software.backend.enums.Level;
import com.software.backend.enums.WorkLocation;

import java.util.Objects;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobDto {
    private Integer id;
    private String title;
    private String description;
    private String category;
    private String location;
    private WorkLocation workLocation;
    private CompanyDto company;
    private LocalDateTime postedAt;
    private LocalDateTime applicationDeadline;
    private Integer salary;     // to be put if needed in frontend
    private EmploymentType employmentType;
    private Level level;
    private List<SectionDto> sections; // sections with fields
    private List<String> staticSections; // static sections
    private List<FieldDto> fields;     // standalone fields

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobDto jobDto = (JobDto) o;
        return Objects.equals(id, jobDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

//     private String salary;
//     private JobLevel level;

}
