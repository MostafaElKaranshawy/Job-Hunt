package com.software.backend.mapper;


import com.software.backend.dto.ReportedApplicantDto;
import com.software.backend.entity.ReportedApplicant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportedApplicantMapper {

    @Mapping(source = "applicant", target = "applicant")
    @Mapping(source = "company", target = "company")
    @Mapping(source = "applicantReportReason", target = "applicantReportReason")
    @Mapping(source = "applicant.user.email", target = "email")
    @Mapping(source = "applicant.user.phoneNumber", target = "phoneNumber")
    @Mapping(source = "applicant.user.username", target = "username")
    ReportedApplicantDto toDto(ReportedApplicant reportedApplicant);


}
