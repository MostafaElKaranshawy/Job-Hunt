package com.software.backend.mapper;

import com.software.backend.dto.EducationDTO;
import com.software.backend.entity.Education;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EducationMapper {

    EducationDTO toDTO(Education education);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicant", ignore = true)
    Education toEntity(EducationDTO educationDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicant", ignore = true)
    void updateEntityFromDTO(EducationDTO updatedEducationDTO,@MappingTarget Education educationToUpdate);
}
