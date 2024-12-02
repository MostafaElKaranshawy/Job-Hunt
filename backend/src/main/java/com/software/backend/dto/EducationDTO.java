package com.software.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.software.backend.entity.Applicant;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class EducationDTO {
    private Integer id;
    private String degree;
    private String institution;
    private String fieldOfStudy;
    private Integer startDate;
    private Integer endDate;
    @JsonIgnore
    private Applicant applicant;
    @Override
    public String toString() {
        return "EducationDTO{" +
                "degree='" + degree + '\'' +
                ", institution='" + institution + '\'' +
                ", fieldOfStudy='" + fieldOfStudy + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

}
