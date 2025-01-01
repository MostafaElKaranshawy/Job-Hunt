package com.software.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.software.backend.enums.EmploymentType;
import com.software.backend.enums.JobStatus;
import com.software.backend.enums.Level;
import com.software.backend.enums.WorkLocation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.OPEN;

    @Column
    private Integer salary;     // to be put if needed in frontend

    @Column
    @Enumerated(EnumType.STRING)
    private Level level;


    @CreationTimestamp
    @Column(
        nullable = false,
        name = "posted_at"
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime postedAt;

    @Column(name = "application_deadline")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime applicationDeadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_location")
    private WorkLocation workLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type")
    private EmploymentType employmentType;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Section> sections;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Field> fields;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedJob> savedJobs;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> jobApplications;
}

