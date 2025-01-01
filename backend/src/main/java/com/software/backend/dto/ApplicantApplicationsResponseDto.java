package com.software.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApplicantApplicationsResponseDto {
    private Integer applicationId;
    private String jobTitle;
    private String applicationStatus;
    private LocalDateTime applicationDate;
    private List<BriefApplicationResponseDto> responses;

    private String companyName;
    private String companyAddress;
    private String companyWebsite;
}
