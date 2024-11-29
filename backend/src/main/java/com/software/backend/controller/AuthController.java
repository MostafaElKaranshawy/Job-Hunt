package com.software.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.software.backend.dto.GoogleAuthDto;
import com.software.backend.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {  // Constructor injection
        this.authService = authService;
    }

    // Google Sign-Up Endpoint
    @PostMapping("/google/signUp")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> googleSignUp(@RequestBody GoogleAuthDto googleAuthDto) {
        System.out.println("Google Sign-Up Endpoint");
        System.out.println(googleAuthDto.getIdToken());

        boolean created = authService.googleSignUp(googleAuthDto);
        return ResponseEntity.ok(created);
    }



}