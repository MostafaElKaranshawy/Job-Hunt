package com.software.backend.entity.compositeKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class JobFieldsOrderId implements Serializable {

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "field_id")
    private Integer fieldId;

}
