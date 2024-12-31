package com.software.backend.entity;

import com.software.backend.enums.JobReportReason;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "reported_job")
public class ReportedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer id;

    @Column(name = "report_reason", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobReportReason jobReportReason;

    @Column(name = "report_description")
    private String reportDescription;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "reported_by", nullable = false)
    private Applicant applicant;
}