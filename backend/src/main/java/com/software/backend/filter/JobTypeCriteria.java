package com.software.backend.filter;

import com.software.backend.dto.JobDto;
import com.software.backend.entity.Job;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobTypeCriteria implements JobsFilterCriteria{

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobMapper mapper;


    @Override
    public List<JobDto> meetCriteria(String data){

        List<Job> jobs = jobRepository.findAllByTypeContainsIgnoreCase(data).orElse(new ArrayList<>());

        return jobs.stream().map(mapper::jobToJobDto).collect(Collectors.toList());
    }
}
