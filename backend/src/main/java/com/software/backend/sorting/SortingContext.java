package com.software.backend.sorting;

import com.software.backend.dto.JobDto;

import java.util.List;

public class SortingContext {
    private SortingTechnique sortingTechnique;

    public SortingContext (String sortingTechnique) {
        System.out.println("SortingContext: " + sortingTechnique);
        switch (sortingTechnique) {
            case "DateAsc":
                System.out.println("DateAsc");
                this.sortingTechnique = new SortingByDateAscending();
                break;
            case "DateDesc":
                System.out.println("DateDesc");
                this.sortingTechnique = new SortingByDateDescending();
                break;
            case "SalaryAsc":
                System.out.println("SalaryAsc");
                this.sortingTechnique = new SortingBySalaryAscending();
                break;
            case "SalaryDesc":
                System.out.println("SalaryDesc");
                this.sortingTechnique = new SortingBySalaryDescending();
                break;
        }
    }

    public List<JobDto> sortJobs(List<JobDto> jobs) {
        return sortingTechnique.sortJobs(jobs);
    }
}
