package com.software.backend.mapper;

import com.software.backend.dto.JobDto;
import com.software.backend.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobMapper {
    @Mapping(source = "company", target = "company")
    JobDto jobToJobDto(Job job);

    @Mapping(source = "company", target = "company")
    Job jobDtoToJob(JobDto jobDto);
}
