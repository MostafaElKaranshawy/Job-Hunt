package com.software.backend.controller;

import com.software.backend.dto.EducationDTO;
import com.software.backend.service.ApplicantServices;
import com.software.backend.service.EducationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "users")
@CrossOrigin
public class EducationController {
    @Autowired
    EducationServices service;
    @PostMapping("/{id}/profile/education")
    public void addEducation(@PathVariable Integer id, @RequestBody EducationDTO dto){
        service.addEducation(id, dto);
    }
    @GetMapping("/{id}/profile/education")
    public List<EducationDTO> getEducation(@PathVariable Integer id){
        return service.getEducation(id);
    }
    @PutMapping("/{id}/profile/education")
    public void updateEducation(@PathVariable Integer id, @RequestBody EducationDTO dto){
        service.updateEducation(id, dto);
    }
}
