package com.software.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "saved_job")
public class SavedJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saved_job_id")
    private Integer savedJobId;

    @ManyToOne
    @JoinColumn(
        name = "job_id",
        referencedColumnName = "job_id",
        nullable = false
    )
    private Job job;

    @ManyToOne
    @JoinColumn(
            name = "applicant_id",
            referencedColumnName = "applicant_id",
            nullable = false
    )
    private Applicant applicant;

    @Column(
        name = "created_at",
        nullable = false
    )
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "savedJob{" +
                "savedJobId=" + savedJobId +
                ", job=" + job +
                ", applicant=" + applicant +
                ", createdAt=" + createdAt +
                '}';
    }
}
