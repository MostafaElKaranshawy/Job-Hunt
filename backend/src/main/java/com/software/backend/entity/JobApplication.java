package com.software.backend.entity;

import com.software.backend.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Integer id;

    @Column(
        name = "application_status",
        nullable = false
    )
    private ApplicationStatus applicationStatus;


    @CreationTimestamp
    @Column(
        name = "application_date",
        nullable = false,
        updatable = false
    )
    private LocalDateTime applicationDate;

    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationResponse> applicationResponsesList;

    @ManyToOne
    @JoinColumn(name = "applicant_id", referencedColumnName = "applicant_id", nullable = false)
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
}
