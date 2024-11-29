package com.software.backend.entity;

import com.software.backend.enums.JobStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
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

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private JobStatus status;

    @CreationTimestamp
    @Column(
        nullable = false,
        name = "posted_at"
    )
    private LocalDateTime postedAt;

//    private String salary;     // to be put if needed in frontend


    @Column(name = "application_deadline")
    private LocalDateTime applicationDeadline;


    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Section> sections;

}

