package com.software.backend.mapper;


import com.software.backend.dto.ReportedJobDto;
import com.software.backend.entity.ReportedJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportedJobMapper {

    @Mapping(source = "applicant", target = "applicant")
    @Mapping(source = "job", target = "job")
    @Mapping(source = "jobReportReason", target = "jobReportReason")
    ReportedJobDto toDto(ReportedJob reportedJob);
}
