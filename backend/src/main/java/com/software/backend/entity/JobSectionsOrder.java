package com.software.backend.entity;

import com.software.backend.entity.compositeKeys.JobSectionsOrderId;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@Table(name = "job_sections_order")
public class JobSectionsOrder {

    @EmbeddedId
    private JobSectionsOrderId id;

    @Column(name = "section_order", nullable = false)
    private Integer sectionOrder;

    @ManyToOne
    @MapsId("jobId")
    @JoinColumn(name = "job_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Job job;

    @ManyToOne
    @MapsId("sectionId")
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

}
