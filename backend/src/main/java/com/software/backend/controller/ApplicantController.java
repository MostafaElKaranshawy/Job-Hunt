package com.software.backend.controller;

import com.software.backend.dto.ApplicantDTO;
import com.software.backend.dto.JobDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.software.backend.service.ApplicantServices;

import java.util.List;

@RestController
@RequestMapping(path = "users")
@CrossOrigin
public class ApplicantController {
    @Autowired
    ApplicantServices service;

    @GetMapping("/{username}/profile")
    public ResponseEntity<ApplicantDTO> getApplicant(@PathVariable String username){
        ApplicantDTO dto = service.getApplicant(username);
        if(dto != null)
            return new ResponseEntity<>(dto, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/{username}/profile")
    public ResponseEntity<String> updateApplicant(@PathVariable String username, @RequestBody ApplicantDTO dto){
        if(service.updateApplicant(username,dto))
            return ResponseEntity.ok("Applicant updated successfully");
        else
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
    @GetMapping("/{username}/profile/skills")
    public ResponseEntity<List<String>> getSkills(@PathVariable String username){
        try {
            List<String> skills = service.getSkills(username);
            if(skills != null)
                return new ResponseEntity<>(skills, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @PutMapping("/{username}/profile/skills")
    public ResponseEntity<String> setSkills(@PathVariable String username, @RequestBody List<String> skills){
        if(service.setSkills(username,skills))
            return ResponseEntity.ok("Skills updated successfully");
        else
            return new ResponseEntity<>("User has Empty skills List or the User not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{username}/profile/savedJobs")
    public ResponseEntity<List<JobDto>> getSavedJobs(@PathVariable String username,
                                                     @RequestParam(name =  "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "offset", defaultValue = "5") int offset){
        try {
            System.out.println(username);
            System.out.println(page);
            System.out.println(offset);
            List<JobDto> jobs = service.getSavedJobs(username, page, offset);
            if(jobs != null)
                return new ResponseEntity<>(jobs, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
