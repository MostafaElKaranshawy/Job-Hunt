package com.software.backend.filter;

import com.software.backend.dto.JobDto;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class JobCriteriaRunner {
    private final JobsFilterCriteria jobTypeCriteria;

    @Autowired
    public JobCriteriaRunner(JobsFilterCriteria jobTypeCriteria) {
        this.jobTypeCriteria = jobTypeCriteria;
    }

    public List<JobDto> matchCriterias(HashMap<String, String> criterias){
        for(String criteria:criterias.keySet()){

            if(criteria.equals("type")){
                return jobTypeCriteria.meetCriteria(criterias.get(criteria));
            }
        }
        return new ArrayList<>();
    }

}

