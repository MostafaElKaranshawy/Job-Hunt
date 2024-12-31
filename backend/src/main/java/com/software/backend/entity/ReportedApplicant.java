package com.software.backend.entity;


import com.software.backend.enums.ApplicantReportReason;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reported_applicant")
public class ReportedApplicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer id;


    @Column(name = "report_reason", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicantReportReason applicantReportReason;

    @Column(name = "report_description")
    private String reportDescription;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
}