package com.software.backend.entity.compositeKeys;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class JobSectionsOrderId implements Serializable {

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "section_id")
    private Integer sectionId;
}
