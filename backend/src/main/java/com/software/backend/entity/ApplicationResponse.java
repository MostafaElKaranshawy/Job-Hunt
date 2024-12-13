package com.software.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "application_response")
public class ApplicationResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private Integer id;

    @Column(name = "response_data", nullable = false)
    private String responseData;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @ManyToOne
    @JoinColumn(name = "application_id", referencedColumnName = "application_id", nullable = false)
    private JobApplication jobApplication;
}
