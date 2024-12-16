package com.software.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.software.backend.entity.Applicant;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienceDTO {
    private Integer id;
    private String title;
    private String company;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    @JsonIgnore
    private Applicant applicant;

    @Override
    public String toString() {
        return "ExperienceDTO {" +
                "id='" + id +
                ", title=" + title +
                ", company=" + company +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", description=" + description +
                '}';
    }
}
