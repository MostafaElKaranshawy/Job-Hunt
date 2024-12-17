package com.software.backend.controller;

import com.software.backend.dto.HomeDto;
import com.software.backend.dto.JobDto;
import com.software.backend.enums.WorkLocation;
import com.software.backend.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("")
@CrossOrigin
public class JobController {

    @Autowired
    private JobService jobService;

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
    public ResponseEntity<HomeDto> filterJobs(
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

            HomeDto homeDto = jobService.filterJobs(employmentType, workLocation, category, salary, level, query, sort, page, offset);

            return ResponseEntity.ok(homeDto);
        } catch (Exception e) {
            e.printStackTrace();
            HomeDto homeDto = new HomeDto();
            homeDto.setTotalJobs(0);
            homeDto.setJobs(new ArrayList<>());
            return ResponseEntity.status(500).body(homeDto);
        }
    }
}

