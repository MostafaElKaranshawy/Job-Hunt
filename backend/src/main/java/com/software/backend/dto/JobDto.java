package com.software.backend.dto;

import com.software.backend.entity.Company;

import java.time.LocalDateTime;
import java.util.Objects;


public class JobDto {
    private Integer id;
    private String title;
    private String description;
    private String category;
    private String location;
    private CompanyDto company;
    private LocalDateTime postedAt;
    private LocalDateTime applicationDeadline;
    private Integer salary;     // to be put if needed in frontend
    private String type;
    private String level;

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    @Override
    public String toString() {
        return "JobDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", company=" + company +
                ", postedAt=" + postedAt +
                ", applicationDeadline=" + applicationDeadline +
                ", salary=" + salary +
                ", type='" + type + '\'' +
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
}
