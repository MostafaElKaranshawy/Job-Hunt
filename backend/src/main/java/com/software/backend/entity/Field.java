package com.software.backend.entity;

import com.software.backend.enums.FieldType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "field")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Integer id;

    @Column(
        name = "field_type",
        nullable = false
    )
    private FieldType fieldType;

    @Column
    private List<String> options;


    @Column(nullable = false)
    private String label;

    @Column(
        nullable = false,
        name = "is_required"
    )
    private Boolean isRequired;

    @Column(
        name = "field_order",
        nullable = false
    )
    private Integer fieldOrder;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;


    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

}

/*
*   options is to be further inspected when implementing the frontend
*           and to check how will it be dealt with in java
*
*
* */
