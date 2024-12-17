package com.software.backend.dto;

import com.software.backend.enums.EmploymentType;
import com.software.backend.enums.Level;
import com.software.backend.enums.WorkLocation;

import java.util.Objects;
import com.software.backend.entity.Company;
// import com.software.backend.enums.JobLevel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobDto {
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
//    private E

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }



    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public WorkLocation getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(WorkLocation workLocation) {
        this.workLocation = workLocation;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public LocalDateTime getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDateTime applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "JobDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", workLocation=" + workLocation +
                ", company=" + company +
                ", postedAt=" + postedAt +
                ", applicationDeadline=" + applicationDeadline +
                ", salary=" + salary +
                ", employementType=" + employmentType +
                ", level='" + level + '\'' +
                '}';
    }

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
