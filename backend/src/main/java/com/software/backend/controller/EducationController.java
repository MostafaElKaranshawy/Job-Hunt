package com.software.backend.controller;

import com.software.backend.dto.EducationDTO;
import com.software.backend.service.EducationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "users")
@CrossOrigin
public class EducationController {
    @Autowired
    EducationServices service;
    @PostMapping("/{id}/profile/education")
    public ResponseEntity<String> addEducation(@PathVariable Integer id, @RequestBody EducationDTO dto){
        if(service.addEducation(id, dto))
            return ResponseEntity.ok("Education added successfully");
        else
            return new ResponseEntity<>("Applicant not found", HttpStatus.NOT_FOUND);
    }
    @GetMapping("/{id}/profile/education")
    public List<EducationDTO> getEducation(@PathVariable Integer id){
        return service.getEducation(id);
    }
    @PutMapping("/{id}/profile/education")
    public ResponseEntity<String> updateEducation(@PathVariable Integer id, @RequestBody EducationDTO dto){
        if(service.updateEducation(id, dto))
            return ResponseEntity.ok("Education updated successfully");
        else
            return new ResponseEntity<>("Education not found", HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{educationId}/profile/education")
    public ResponseEntity<String> deleteEducation(@PathVariable Integer educationId){
        if(service.deleteEducation(educationId))
            return ResponseEntity.ok("Education deleted successfully");
        else
            return new ResponseEntity<>("Education not found", HttpStatus.NOT_FOUND);
    }
}
