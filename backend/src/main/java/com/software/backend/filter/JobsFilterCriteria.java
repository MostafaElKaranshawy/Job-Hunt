package com.software.backend.filter;

import com.software.backend.dto.JobDto;
import com.software.backend.entity.Job;
import java.util.List;

public interface JobsFilterCriteria {
    public List<JobDto> meetCriteria(String data);
}
