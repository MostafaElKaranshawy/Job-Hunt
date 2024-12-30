package com.software.backend.dto;

import com.software.backend.entity.JobReport;
import lombok.Data;

@Data
public class  JobReportDTO {
    private Integer id;
    private String reportReason;
    private String reportDescription;
    private String reportStatus;

    public JobReportDTO(Integer id, String reportReason, String reportDescription, String reportStatus) {
        this.id = id;
        this.reportReason = reportReason;
        this.reportDescription = reportDescription;
        this.reportStatus = reportStatus;
    }

    public static JobReportDTO fromEntity(JobReport jobReport) {
        return new JobReportDTO(
                jobReport.getId(),
                jobReport.getReportReason(),
                jobReport.getReportDescription(),
                jobReport.getReportStatus().toString()
        );
    }
}
