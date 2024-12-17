package com.software.backend.filter;

import com.software.backend.dto.JobDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JobCriteriaRunner {
    private final JobsFilterCriteria jobTypeCriteria;
    private final JobsFilterCriteria jobCategoryCriteria;
    private final JobsFilterCriteria jobLocationCriteria;
    private final JobsFilterCriteria jobSalaryCriteria;
    private final JobsFilterCriteria jobLevelCriteria;
    private final JobsFilterCriteria jobSearchCriteria;

    @Autowired
    public JobCriteriaRunner(JobsFilterCriteria jobTypeCriteria,
                             JobsFilterCriteria jobCategoryCriteria,
                             JobsFilterCriteria jobLocationCriteria,
                             JobsFilterCriteria jobSalaryCriteria,
                             JobsFilterCriteria jobLevelCriteria,
                             JobsFilterCriteria jobSearchCriteria) {
        this.jobTypeCriteria = jobTypeCriteria;
        this.jobCategoryCriteria = jobCategoryCriteria;
        this.jobLocationCriteria = jobLocationCriteria;
        this.jobSalaryCriteria = jobSalaryCriteria;
        this.jobLevelCriteria = jobLevelCriteria;
        this.jobSearchCriteria = jobSearchCriteria;
    }

    public List<JobDto> matchCriteria(HashMap<String, String> criterias){

        Set<JobDto> jobs = null;

        for (String criteria : criterias.keySet()) {

            String data = criterias.get(criteria);
            Set<JobDto> filteredJobs;

            switch (criteria) {
                case "employmentType" -> filteredJobs = new HashSet<>(jobTypeCriteria.meetCriteria(data));

                case "category" -> filteredJobs = new HashSet<>(jobCategoryCriteria.meetCriteria(data));

                case "workLocation" -> filteredJobs = new HashSet<>(jobLocationCriteria.meetCriteria(data));

                case "salary" -> filteredJobs = new HashSet<>(jobSalaryCriteria.meetCriteria(data));

                case "level" -> filteredJobs = new HashSet<>(jobLevelCriteria.meetCriteria(data));

                case "search" -> filteredJobs = new HashSet<>(jobSearchCriteria.meetCriteria(data));

                default -> { continue; }
            }
            if (jobs == null) {
                jobs = filteredJobs;
            } else {
                jobs.retainAll(filteredJobs);
            }
        }
        return new ArrayList<>(jobs == null ? Set.of() : jobs);
    }

}

