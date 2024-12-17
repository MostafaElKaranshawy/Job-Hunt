package com.software.backend.mapper;

import com.software.backend.dto.FieldDto;
import com.software.backend.entity.Field;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FieldMapper {
    Field fieldDtoToField(FieldDto fieldDto);
}
