package com.software.backend.dto;

import com.software.backend.enums.ApplicantReportReason;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportedApplicantDto {
    private Integer id;
    private String reportDescription;
    private ApplicantReportReason applicantReportReason;
    private LocalDateTime createdAt;
    private ApplicantDTO applicant;
    private CompanyDto company;

    private String email;
    private String username;
    private String phoneNumber;
}
