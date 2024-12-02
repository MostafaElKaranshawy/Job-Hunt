package com.software.backend.mapper;

import com.software.backend.dto.ExperienceDTO;
import com.software.backend.entity.Experience;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ExperienceMapper {
    ExperienceDTO toDTO(Experience experience);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicant", ignore = true)
    Experience toEntity(ExperienceDTO experienceDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicant", ignore = true)
    void updateEntityFromDTO(ExperienceDTO updatedexperienceDTO,@MappingTarget Experience experienceToUpdate);
}
