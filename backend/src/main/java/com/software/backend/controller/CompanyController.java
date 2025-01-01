package com.software.backend.controller;

import com.software.backend.dto.CompanyDto;
import com.software.backend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@CrossOrigin
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @GetMapping("/{companyUsername}")
    public ResponseEntity<?> getCompanyInfo(@PathVariable String companyUsername) {
        CompanyDto companyDto = companyService.getCompanyInfo(companyUsername);
        if(companyDto != null) {
            return ResponseEntity.ok(companyDto);
        }
        return ResponseEntity.status(404).body("Company not found");
    }


    @PatchMapping("/{companyUsername}")
    public ResponseEntity<?> updateCompanyInfo(@PathVariable String companyUsername, @RequestBody CompanyDto companyDto) {
        CompanyDto updatedCompanyDto = companyService.updateCompanyInfo(companyUsername, companyDto);
        if(updatedCompanyDto != null) {
            return ResponseEntity.ok(updatedCompanyDto);
        }
        return ResponseEntity.status(404).body("Company not found or update failed");
    }


    @GetMapping("/{companyUsername}/jobs")
    public ResponseEntity<?> getCompanyJobs(@PathVariable String companyUsername) {
        return ResponseEntity.ok(companyService.getCompanyJobs(companyUsername));
    }

    @DeleteMapping("/{companyUsername}/jobs/{jobId}")
    public ResponseEntity<?> deleteJob(@PathVariable String companyUsername, @PathVariable Integer jobId) {
        if(companyService.deleteJob(companyUsername, jobId)) {
            return ResponseEntity.ok("Job deleted successfully");
        }
        return ResponseEntity.status(404).body("Job not found or delete failed");
    }


}
