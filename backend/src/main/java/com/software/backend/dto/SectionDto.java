package com.software.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class SectionDto {
    private String name;
    private List<FieldDto> fields;
    private List<String> label;
    private List<String> type;
    private List<List<String>> options;
    private List<Boolean> isRequired;
}
