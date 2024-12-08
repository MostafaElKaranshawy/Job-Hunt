package com.software.backend.controller;

import com.software.backend.dto.JobDto;
import com.software.backend.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/company")
@CrossOrigin
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/{companyUsername}/jobs")
    public ResponseEntity<?> getJobs(@PathVariable String companyUsername) {
        System.out.println("@@@@@@@@@@@@@@Company Username: " + companyUsername);
        try {
            System.out.println("@@@@@@@@@@@@@@@Getting Jobs for Company: " + companyUsername);
            List<JobDto> activeJobDtos = jobService.getActiveJobsForCompany(companyUsername);
            List<JobDto> expiredJobDtos = jobService.getExpiredJobsForCompany(companyUsername);

            System.out.println("@@@@@@@@@@@@@@@Active Jobs: " + (activeJobDtos.isEmpty() ? 0 : activeJobDtos.size()));
            System.out.println("Active Jobs: " + activeJobDtos);
            System.out.println("Expired Jobs: " + expiredJobDtos);

            System.out.println("Expired Jobs: " + (expiredJobDtos.isEmpty() ? 0 : expiredJobDtos.size()));

            return ResponseEntity.ok(Map.of(
                    "active", activeJobDtos.isEmpty() ? new ArrayList<>() : activeJobDtos,
                    "expired", expiredJobDtos.isEmpty() ? new ArrayList<>() : expiredJobDtos
            ));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("@@@@@@@@@@@@@@@Error: " + e.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}

