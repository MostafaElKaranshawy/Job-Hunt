package com.software.backend.sorting;

import com.software.backend.dto.JobDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortingByDateAscending implements SortingTechnique {
    @Override
    public List<JobDto> sortJobs(List<JobDto> jobs) {
        // Create a mutable copy of the input list
        List<JobDto> mutableJobs = new ArrayList<>(jobs);
        // Sort the mutable list
        mutableJobs.sort(Comparator.comparing(JobDto::getPostedAt));
        return mutableJobs;
    }
}
