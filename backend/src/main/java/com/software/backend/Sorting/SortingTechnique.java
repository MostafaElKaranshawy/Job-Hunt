package com.software.backend.Sorting;

import com.software.backend.dto.JobDto;

import java.util.List;

public interface SortingTechnique {
    public List<JobDto> sortJobs(List<JobDto> jobs);
}
