package com.software.backend.controller;


import com.software.backend.dto.ReportedApplicantDto;
import com.software.backend.dto.ReportedJobDto;
import com.software.backend.entity.ReportedApplicant;
import com.software.backend.entity.ReportedJob;
import com.software.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @PatchMapping("/reported-applicants/{applicantId}/ban")
    public ResponseEntity<?> banUser(
            @PathVariable Integer applicantId
    ){
        return ResponseEntity.ok(adminService.banUser(applicantId));
    }

    @GetMapping("/reported-jobs")
    public ResponseEntity<Page<ReportedJobDto>> getReportedJobs(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "offset", defaultValue = "5") Integer offset
    ){

        return ResponseEntity.ok(adminService.getReportedJobs(page, offset));
    }


    @GetMapping("/reported-applicants")
    public ResponseEntity<Page<ReportedApplicantDto>> getReportedApplicants(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "offset", defaultValue = "5") Integer offset
    ){
        return ResponseEntity.ok(adminService.getReportedApplicants(page, offset));
    }


    @DeleteMapping("/reported-jobs/{jobReportId}/report")
    public ResponseEntity<?> deleteReportedJob(
            @PathVariable Integer jobReportId
    ){
        return ResponseEntity.ok(adminService.deleteReportedJob(jobReportId));
    }


    @DeleteMapping("/reported-applicants/{applicantReportId}/report")
    public ResponseEntity<?> deleteReportedApplicant(
            @PathVariable Integer applicantReportId
    ){
        return ResponseEntity.ok(adminService.deleteReportedApplicant(applicantReportId));
    }


    @DeleteMapping("/reported-jobs/{jobId}")
    public ResponseEntity<?> deleteJob(
            @PathVariable Integer jobId
    ){
        return ResponseEntity.ok(adminService.deleteJob(jobId));
    }


}
