package com.software.backend.mapper;


import com.software.backend.dto.ReportedApplicantDto;
import com.software.backend.entity.ReportedApplicant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportedApplicantMapper {

    @Mapping(source = "applicant", target = "applicant")
    @Mapping(source = "job", target = "job")
    @Mapping(source = "applicantReportReason", target = "applicantReportReason")
    ReportedApplicantDto toDto(ReportedApplicant reportedApplicant);


}
