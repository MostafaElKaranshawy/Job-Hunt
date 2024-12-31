package com.software.backend.mapper;

import com.software.backend.dto.ApplicantApplicationsResponseDto;
import com.software.backend.dto.ApplicationResponseDTO;
import com.software.backend.entity.ApplicationResponse;
import com.software.backend.entity.JobApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

    @Mapping(source = "job.company.name", target = "companyName")
    @Mapping(source = "job.company.location", target = "companyAddress")
    @Mapping(source = "job.company.website", target = "companyWebsite")
    @Mapping(source = "applicationResponsesList", target = "responses")
    @Mapping(source = "job.title", target = "jobTitle")
    ApplicantApplicationsResponseDto toApplicantApplicationsResponseDto(JobApplication application);

    @Mapping(source = "field.label", target = "fieldName")
    ApplicationResponseDTO toApplicationResponseDTO(ApplicationResponse response);
}

