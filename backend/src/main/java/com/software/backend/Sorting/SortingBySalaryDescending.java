package com.software.backend.Sorting;

import com.software.backend.dto.JobDto;

import java.util.Comparator;
import java.util.List;

public class SortingBySalaryDescending implements SortingTechnique {
    @Override
    public List<JobDto> sortJobs(List<JobDto> jobs) {
        jobs.sort(Comparator.comparing(JobDto::getSalary).reversed());
        return jobs;
    }
}
