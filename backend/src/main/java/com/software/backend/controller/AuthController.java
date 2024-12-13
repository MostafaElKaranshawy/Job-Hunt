package com.software.backend.controller;

import com.software.backend.auth.AuthenticationResponse;
import com.software.backend.entity.RefreshToken;
import com.software.backend.service.RefreshTokenService;
import com.software.backend.service.UserAuthService;
import com.software.backend.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.software.backend.dto.SignUpRequest;
import com.software.backend.service.ApplicantAuthService;
import com.software.backend.service.CompanyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private ApplicantAuthService applicantAuthService;

    @Autowired
    private CompanyAuthService companyAuthService;

    @Autowired
    private UserAuthService userService;

    // Google Sign-Up Endpoint
    @PostMapping("/signup/applicant/google")
    public ResponseEntity<AuthenticationResponse> googleSignUp(@RequestBody SignUpRequest request) {
        System.out.println("Google Sign-Up Endpoint");
        return ResponseEntity.ok(applicantAuthService.applicantGoogleSignUp(request));
    }

    @PostMapping("/login/applicant/google")
    public ResponseEntity<?> googleLogin(
            @RequestBody SignUpRequest request,
            HttpServletResponse response
    ) {
        System.out.println("Google Login Endpoint");
        AuthenticationResponse authenticationResponse = applicantAuthService.loginWithGoogle(request);
        userService.storeTokens(authenticationResponse, response);
        System.out.println("Google Login Endpoint " + authenticationResponse.getUsername());
        AuthenticationResponse usernameObject = new AuthenticationResponse();
        usernameObject.setUsername(authenticationResponse.getUsername());
        return ResponseEntity.ok(usernameObject);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody SignUpRequest request,
            HttpServletResponse response
    ) {
        AuthenticationResponse authenticationResponse = userService.login(request);
        userService.storeTokens(authenticationResponse, response);
        return ResponseEntity.ok(authenticationResponse.getUsername());
    }

    // normal sign up
    @CrossOrigin(origins = "*")
    @PostMapping("/signup/applicant")
    public ResponseEntity<?> signUpApplicant(@RequestBody SignUpRequest signUpRequest ) {
        System.out.println("welcome from applicant signup");
        return ResponseEntity.ok(applicantAuthService.signUp(signUpRequest));
    }

    @PostMapping("/signup/company")
    public ResponseEntity<AuthenticationResponse> signUpCompany(@RequestBody SignUpRequest signUpRequest) {
            return ResponseEntity.ok(companyAuthService.signUp(signUpRequest));
    }


    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        userAuthService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully. You can now log in.");
    }
}

