package com.software.backend.controller;

import com.software.backend.auth.AuthenticationResponse;
import com.software.backend.service.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.service.ApplicantAuthService;
import com.software.backend.service.CompanyAuthService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {


    @Autowired
    private ApplicantAuthService applicantAuthService;

    @Autowired
    private CompanyAuthService companyAuthService;

    @Autowired
    private UserAuthService service;

    // Google Sign-Up Endpoint
    @PostMapping("/signup/applicant/google")
    public ResponseEntity<AuthenticationResponse> googleSignUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(applicantAuthService.applicantGoogleSignUp(request));
    }

    // normal sign up
    @PostMapping("/signup/applicant")
    public ResponseEntity<AuthenticationResponse> signUpApplicant(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(applicantAuthService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody SignUpRequest request
    ) {
        return ResponseEntity.ok(service.login(request));
    }


    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }



    @PostMapping("/signup/company")
    public ResponseEntity<AuthenticationResponse> signUpCompany(@RequestBody SignUpRequest signUpRequest) {
            return ResponseEntity.ok(companyAuthService.signUp(signUpRequest));
    }
}