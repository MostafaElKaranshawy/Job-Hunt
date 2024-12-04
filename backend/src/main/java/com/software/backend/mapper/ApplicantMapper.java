package com.software.backend.mapper;

import com.software.backend.dto.ApplicantDTO;
import com.software.backend.entity.Applicant;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ApplicantMapper {

    ApplicantMapper INSTANCE = Mappers.getMapper(ApplicantMapper.class);

    @Mapping(source = "user.username", target = "username")
    ApplicantDTO applicantToApplicantDTO(Applicant applicant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateApplicantFromDTO(ApplicantDTO dto, @MappingTarget Applicant applicant);

}
