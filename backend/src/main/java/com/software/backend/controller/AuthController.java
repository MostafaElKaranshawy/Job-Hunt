package com.software.backend.controller;

import com.software.backend.auth.AuthenticationRequest;
import com.software.backend.auth.AuthenticationResponse;
import com.software.backend.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;
import com.software.backend.service.ApplicantAuthService;
import com.software.backend.service.CompanyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {


    @Autowired
    private ApplicantAuthService applicantAuthService;

    @Autowired
    private CompanyAuthService companyAuthService;

    @Autowired
    private AuthenticationService service;

    // Google Sign-Up Endpoint
    @PostMapping("/signup/applicant/google")
    public ResponseEntity<AuthenticationResponse> googleSignUp(@RequestBody SignUpRequest request) {
        System.out.println("Google sign up");
        return ResponseEntity.ok(applicantAuthService.applicantGoogleSignUp(request));
    }

    // normal sign up
    @PostMapping("/signup/applicant")
    public ResponseEntity<AuthenticationResponse> signUpApplicant(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(applicantAuthService.signUp(signUpRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody SignUpRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }



    @PostMapping("/signup/company")
    public ResponseEntity<?> signUpCompany(@RequestBody SignUpRequest signUpRequest) {
        try {
            companyAuthService.signUp(signUpRequest);
            return ResponseEntity.ok("Company signup successful.");
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }
}