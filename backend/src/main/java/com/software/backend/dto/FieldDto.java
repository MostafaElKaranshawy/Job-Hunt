package com.software.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class FieldDto {
    private String type;
    private String label;
    private Boolean isRequired;
    private List<String> options;

    public FieldDto(String label, String type, boolean isRequired, List<String> options) {
        this.label = label;
        this.type = type;
        this.isRequired = isRequired;
        this.options = options;
    }
}
