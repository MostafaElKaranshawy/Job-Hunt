package com.software.backend.mapper;


import com.software.backend.dto.SectionDto;
import com.software.backend.entity.Section;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SectionMapper {
    Section sectionDtoToSection(SectionDto sectionDto);
}
