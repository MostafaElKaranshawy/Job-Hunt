package com.software.backend.controller;

import com.software.backend.dto.ApplicantDTO;
import com.software.backend.service.ApplicantServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "users")
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
    public void updateApplicant(@PathVariable String username, @RequestBody ApplicantDTO dto){
        service.updateApplicant(username,dto);
    }

}
