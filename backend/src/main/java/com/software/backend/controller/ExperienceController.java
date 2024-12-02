package com.software.backend.controller;

import com.software.backend.dto.ExperienceDTO;
import com.software.backend.service.ExperienceServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "users")
@CrossOrigin
public class ExperienceController {
    @Autowired
    ExperienceServices service;

    @PostMapping("/{id}/profile/experience")
    public ResponseEntity<String> addExperience(@PathVariable Integer id, @RequestBody ExperienceDTO dto){
        if(service.addExperience(id, dto))
            return ResponseEntity.ok("Experience added successfully");
        else
            return new ResponseEntity<>("Applicant not found", HttpStatus.NOT_FOUND);
    }
    @GetMapping("/{id}/profile/experience")
    public List<ExperienceDTO> getExperience(@PathVariable Integer id){
        return service.getExperience(id);
    }
    @PutMapping("/{id}/profile/experience")
    public ResponseEntity<String> updateExperience(@PathVariable Integer id, @RequestBody ExperienceDTO dto){
        if(service.updateExperience(id, dto))
            return ResponseEntity.ok("Experience updated successfully");
        else
            return new ResponseEntity<>("Experience not found", HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{experienceId}/profile/experience")
    public ResponseEntity<String> deleteExperience(@PathVariable Integer experienceId){
        if(service.deleteExperience(experienceId))
            return ResponseEntity.ok("Experience deleted successfully");
        else
            return new ResponseEntity<>("Experience not found", HttpStatus.NOT_FOUND);
    }

}
