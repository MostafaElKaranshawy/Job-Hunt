package com.software.backend.controller;

import com.software.backend.dto.HomeDto;
import com.software.backend.dto.JobDto;
import com.software.backend.service.JobService;
import com.software.backend.service.StaticSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("")
@CrossOrigin
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private StaticSectionService staticSectionService;


    @GetMapping("/home/{username}/jobs/filter")
    public ResponseEntity<HomeDto> filterJobs(
            @PathVariable(name = "username") String username,
            @RequestParam(name = "employmentType", defaultValue = "") String employmentType,
            @RequestParam(name = "workLocation", defaultValue = "") String workLocation,
            @RequestParam(name = "category", defaultValue = "") String category,
            @RequestParam(name = "salary", defaultValue = "0") String salary,
            @RequestParam(name = "level", defaultValue = "") String level,
            @RequestParam(name = "query", defaultValue = "") String query,
            @RequestParam(name = "sort", defaultValue = "DateDesc") String sort,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "offset", defaultValue = "5") Integer offset){
        try {

            HomeDto homeDto = jobService.handleHomeJobs(username, employmentType, workLocation, category, salary, level, query, sort, page, offset);

            return ResponseEntity.ok(homeDto);
        } catch (Exception e) {
            e.printStackTrace();
            HomeDto homeDto = new HomeDto();
            homeDto.setTotalJobs(0);
            homeDto.setJobs(new ArrayList<>());
            return ResponseEntity.status(500).body(homeDto);
        }
    }
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

    @PostMapping("/home/{username}/jobs/{jobId}/save")
    public ResponseEntity<?> saveJob(@PathVariable(name = "username") String username,
                                     @PathVariable(name = "jobId") Integer jobId) {
        try {
            jobService.saveJob(username, jobId);
            return ResponseEntity.ok("Saved Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @DeleteMapping("/home/{username}/jobs/{jobId}/unsave")
    public ResponseEntity<?> unSaveJob(@PathVariable(name = "username") String username,
                                     @PathVariable(name = "jobId") Integer jobId) {
        try {
            jobService.unSaveJob(username, jobId);
            return ResponseEntity.ok("Unsaved Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

