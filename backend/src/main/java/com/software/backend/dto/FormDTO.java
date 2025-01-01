package com.software.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class FormDTO {
    private List<String> staticSections;
    private List<SectionDto> sections;
    private List<FieldDto> fields;
}
