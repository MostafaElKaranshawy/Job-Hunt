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
    @Mapping(source = "username", target = "user.username")
    Applicant ApplicantDTOToApplicant(ApplicantDTO applicantDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateApplicantFromDTO(ApplicantDTO dto, @MappingTarget Applicant applicant);

}
