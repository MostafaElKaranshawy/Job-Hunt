package com.software.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomeDto {
    List<JobDto> jobs;
    int totalJobs;
}
