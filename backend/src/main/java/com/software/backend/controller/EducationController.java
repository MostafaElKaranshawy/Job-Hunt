package com.software.backend.controller;

import com.software.backend.dto.EducationDTO;
import com.software.backend.service.EducationServices;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "users")
@CrossOrigin
public class EducationController {
    @Autowired
    EducationServices service;
    @PostMapping("/{id}/profile/education")
    public ResponseEntity<String> addEducation(@PathVariable Integer id, @RequestBody EducationDTO dto){
        try {
            if (service.addEducation(id, dto))
                return ResponseEntity.ok("Education added successfully");
            else
                return new ResponseEntity<>("Applicant not found", HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }
    @GetMapping("/{id}/profile/education")
    public ResponseEntity<?> getEducation(@PathVariable Integer id){
        try {
            List<EducationDTO> educationList = service.getEducation(id);
            return ResponseEntity.ok(educationList);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<>("Applicant Not Found", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/profile/education/{educationId}")
    public ResponseEntity<String> updateEducation(@PathVariable Integer educationId, @RequestBody EducationDTO dto){
        if(service.updateEducation(educationId, dto))
            return ResponseEntity.ok("Education updated successfully");
        else
            return new ResponseEntity<>("Education not found", HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/profile/education/{educationId}")
    public ResponseEntity<String> deleteEducation(@PathVariable Integer educationId){
        if(service.deleteEducation(educationId))
            return ResponseEntity.ok("Education deleted successfully");
        else
            return new ResponseEntity<>("Education not found", HttpStatus.NOT_FOUND);
    }
}
