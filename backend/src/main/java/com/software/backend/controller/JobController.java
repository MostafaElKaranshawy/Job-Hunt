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
@RequestMapping("")
@CrossOrigin
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/company/{companyUsername}/jobs")
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
            System.out.println("@@@@@@@@@@@@@@@Error: " + e.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/home/jobs")
    public ResponseEntity<List<JobDto>> getJobs(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                @RequestParam(name = "offset", defaultValue = "5") Integer offset) {
        try {

            List<JobDto> jobs = jobService.getHomeActiveJobs(page, offset);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }

    @GetMapping("/home/jobs/search")
    public ResponseEntity<List<JobDto>> searchJobs(@RequestParam(name = "query", defaultValue = "") String query,
                                                   @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(name = "offset", defaultValue = "5") Integer offset){
        try {

            List<JobDto> jobs = jobService.searchJobs(query, page, offset);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }

    @GetMapping("/home/jobs/filter")
    public ResponseEntity<List<JobDto>> filterJobs(
            @RequestParam(name = "type", defaultValue = "") String type,
            @RequestParam(name = "location", defaultValue = "") String location,
            @RequestParam(name = "category", defaultValue = "") String category,
            @RequestParam(name = "salary", defaultValue = "") String salary,
            @RequestParam(name = "level", defaultValue = "") String level,
            @RequestParam(name = "query", defaultValue = "") String query) {
        try {

            List<JobDto> jobs = jobService.filterJobs(type, location, category, salary, level, query);

            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }
}

