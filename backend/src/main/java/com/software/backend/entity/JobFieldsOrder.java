package com.software.backend.entity;


import com.software.backend.entity.compositeKeys.JobFieldsOrderId;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@Table(name = "job_fields_order")
public class JobFieldsOrder {

    @EmbeddedId
    private JobFieldsOrderId id;

    @Column(name = "field_order", nullable = false)
    private Integer fieldOrder;

    @ManyToOne
    @MapsId("jobId")
    @JoinColumn(name = "job_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Job job;

    @ManyToOne
    @MapsId("fieldId")
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

}
