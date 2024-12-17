package com.software.backend.entity;

import com.software.backend.enums.EmploymentType;
import com.software.backend.enums.JobLevel;
import com.software.backend.enums.WorkLocation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"company", "sections", "fields"})
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column
    private String location;

    @CreationTimestamp
    @Column(
        nullable = false,
        name = "posted_at"
    )
    private LocalDateTime postedAt;

    private String salary;

    @Column(name = "application_deadline")
    private LocalDateTime applicationDeadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "work-location", nullable = false)
    private WorkLocation workLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_level", nullable = false)
    private JobLevel level;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false)
    private EmploymentType employmentType;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Section> sections;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Field> fields;
}

