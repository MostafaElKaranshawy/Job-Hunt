package com.software.backend.mapper;

import com.software.backend.dto.ApplicantDTO;
import com.software.backend.entity.Applicant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ApplicantMapper {

    ApplicantMapper INSTANCE = Mappers.getMapper(ApplicantMapper.class);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "email")
    ApplicantDTO applicantToApplicantDTO(Applicant applicant);
    @Mapping(source = "username", target = "user.username")
    @Mapping(source = "email", target = "user.email")
    Applicant ApplicantDTOToApplicant(ApplicantDTO applicantDTO);
}
