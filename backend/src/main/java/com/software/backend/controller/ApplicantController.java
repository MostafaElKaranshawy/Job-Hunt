package com.software.backend.controller;

import com.software.backend.entity.Applicant;
import com.software.backend.service.ApplicantServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "applicant")
public class ApplicantController {
    @Autowired
    ApplicantServices service;

    @GetMapping("/{id}")
    public Applicant getApplicant(@RequestParam int id){
       return service.getApplicant(id);
    }
    @PutMapping
    public void updateApplicant(@RequestBody Applicant applicant){
        service.updateApplicant(applicant);
    }
}
