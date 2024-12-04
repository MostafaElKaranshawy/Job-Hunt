package com.software.backend.controller;

import com.software.backend.dto.SignUpRequest;
import com.software.backend.exception.BusinessException;
import com.software.backend.service.ApplicantAuthService;
import com.software.backend.service.CompanyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.software.backend.service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ApplicantAuthService applicantAuthService;

    @Autowired
    private CompanyAuthService companyAuthService;

    // Google Sign-Up Endpoint
    @PostMapping("/google/signUp")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> googleSignUp(@RequestBody String idToken) {
        System.out.println("Google Sign-Up Endpoint");
        System.out.println(idToken);
        boolean created = authService.googleSignUp(idToken);
        return ResponseEntity.ok("");
    }
    @PostMapping("/signUp")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> signUp(@RequestBody String idToken) {

        return ResponseEntity.ok("");
    }

    // normal sign up
    @PostMapping("/signup/applicant")
    public ResponseEntity<?> signUpApplicant(@RequestBody SignUpRequest signUpRequest) {
        System.out.println("Applicant sign up");
        try {
            applicantAuthService.signUp(signUpRequest);
            return ResponseEntity.ok("Applicant signup successful.");
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }

//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
//        }
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