package com.software.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompanyDto {
    private Integer id;
    private String name;
    private String location;
    private String website;
    private String overview;
    private String username; // from User entity
    private String email;    // from User entity
}
