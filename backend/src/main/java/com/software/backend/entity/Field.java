package com.software.backend.entity;

import com.software.backend.enums.FieldType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"job", "section"})
@Table(name = "field")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Integer id;

    @Column(
        name = "field_type"
    )
    private FieldType fieldType;

    @Column
    private List<String> options;

    @Column
    private String type;  // can be changed later to enum

    @Column(nullable = false)
    private String label;

    @Column(
        nullable = false,
        name = "is_required"
    )
    private Boolean isRequired;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Job job;
}
