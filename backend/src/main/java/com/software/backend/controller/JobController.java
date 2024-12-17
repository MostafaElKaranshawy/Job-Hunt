package com.software.backend.controller;

import com.software.backend.dto.JobDto;
import com.software.backend.dto.SectionDto;
import com.software.backend.entity.Section;
import com.software.backend.service.JobService;
import com.software.backend.service.StaticSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
@CrossOrigin
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private StaticSectionService staticSectionService;

    @PostMapping("/company/{companyUsername}/jobs/create")
    public ResponseEntity<?> createJob(@PathVariable String companyUsername, @RequestBody JobDto jobDto) {
        System.out.println("Company Username: " + companyUsername);
        System.out.println("Job DTO: " + jobDto);
        try {
            Integer createdJobId = jobService.createJobWithCustomForm(companyUsername, jobDto);
            return ResponseEntity.ok(createdJobId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}

