package com.software.backend.mapper;

import com.software.backend.dto.JobDto;
import com.software.backend.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface JobMapper {
    @Mapping(source = "company", target = "company")
    JobDto jobToJobDto(Job job);
}
