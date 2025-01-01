package com.software.backend.dto;

import com.software.backend.enums.JobReportReason;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportedJobDto {
    private Integer id;
    private JobReportReason jobReportReason;
    private String reportDescription;
    private LocalDateTime createdAt;
    private JobDto job;
    private ApplicantDTO applicant;
}
