package com.software.backend.Sorting;

import com.software.backend.dto.JobDto;
import com.software.backend.entity.Job;

import java.util.List;

public class SortingContext {
    private SortingTechnique sortingTechnique;

    public SortingContext(String sortingTechnique) throws Exception {
        switch (sortingTechnique) {
            case "DateAsc":
                this.sortingTechnique = new SortingByDateAscending();
                break;
            case "DateDesc":
                this.sortingTechnique = new SortingByDateDescending();
                break;
            case "SalaryAsc":
                this.sortingTechnique = new SortingBySalaryAscending();
                break;
            case "SalaryDesc":
                this.sortingTechnique = new SortingBySalaryDescending();
                break;
            default:
                throw new Exception("Invalid sorting technique");
        }
    }

    public List<JobDto> sortJobs(List<JobDto> jobs) {
        return sortingTechnique.sortJobs(jobs);
    }
}
