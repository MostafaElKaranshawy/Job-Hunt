package com.software.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "education")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_id")
    private Integer id; // Primary key for the Education table


    @Column(nullable = false)
    private String degree; // e.g., Bachelor's, Master's, etc.


    @Column(nullable = false)
    private String institution; // Name of the school or university


    @Column(
        nullable = false,
        name = "field_of_study"
    )
    private String fieldOfStudy; // e.g., Computer Science, Engineering, etc.


    @Column(
        nullable = false,
        name = "start_date",
        columnDefinition = "YEAR"
    )
    private Integer startDate;


    @Column(
            name = "end_date"
    )
    private Integer endDate;


    @ManyToOne
    @JoinColumn(
            name = "applicant_id",
            referencedColumnName = "applicant_id",
            nullable = false
    )
    private Applicant applicant; // Many-to-one relationship with Applicant

    @Override
    public String toString() {
        return "Education{" +
                "degree='" + degree + '\'' +
                ", institution='" + institution + '\'' +
                ", fieldOfStudy='" + fieldOfStudy + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", " + applicant.toString() +
                '}';
    }
}