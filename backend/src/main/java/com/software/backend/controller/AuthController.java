package com.software.backend.controller;

import com.software.backend.dto.AuthenticationResponse;
import com.software.backend.dto.ResetPasswordRequest;
import com.software.backend.dto.LogInRequest;
import com.software.backend.dto.ResponseMessage;
import com.software.backend.service.*;
import com.software.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.software.backend.dto.SignUpRequest;
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
    private UserAuthService userAuthService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/hash-password")
    public ResponseEntity<?> hashPassword(@RequestBody String password) {
        String hashedPassword = passwordService.hashPassword(password);
        System.out.println("Hashed password: " + hashedPassword);
        return ResponseEntity.ok().body(hashedPassword);
    }

    // Google Sign-Up Endpoint
    @PostMapping("/signup/applicant/google")
    public ResponseEntity<?> googleSignUp(@RequestBody SignUpRequest request) {
        System.out.println("Google Sign-Up Endpoint");
        applicantAuthService.applicantGoogleSignUp(request);
        ResponseMessage responseMessage = new ResponseMessage("You have successfully signed up with Google. Please login.");
        return ResponseEntity.ok().body(responseMessage);
    }

    @PostMapping("/login/applicant/google")
    public ResponseEntity<?> googleLogin(
            @RequestBody SignUpRequest request,
            HttpServletResponse response
    ) {
        System.out.println("Google Login Endpoint");
        AuthenticationResponse authenticationResponse = applicantAuthService.loginWithGoogle(request);
        tokenService.storeTokens(authenticationResponse, response);
        AuthenticationResponse usernameObject = new AuthenticationResponse();
        usernameObject.setUsername(authenticationResponse.getUsername());
        return ResponseEntity.ok().body(usernameObject);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LogInRequest request,
            HttpServletResponse response
    ) {
            System.out.println("Login Endpoint ");

            AuthenticationResponse authenticationResponse = userAuthService.login(request);
            tokenService.storeTokens(authenticationResponse, response);
            AuthenticationResponse usernameObject = new AuthenticationResponse();
            usernameObject.setUsername(authenticationResponse.getUsername());
            usernameObject.setUserType(authenticationResponse.getUserType());
            System.out.println("Username: " + usernameObject.getUsername());
            return ResponseEntity.ok().body(usernameObject);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(
            @RequestBody LogInRequest request,
            HttpServletResponse response
    ) {
            System.out.println("Admin Login Endpoint ");
            AuthenticationResponse authenticationResponse = userAuthService.adminLogin(request);
            tokenService.storeTokens(authenticationResponse, response);
            AuthenticationResponse usernameObject = new AuthenticationResponse();
            usernameObject.setUsername(authenticationResponse.getUsername());
            usernameObject.setUserType(authenticationResponse.getUserType());
            System.out.println("Username: " + usernameObject.getUsername());
            return ResponseEntity.ok().body(usernameObject);
    }

    // normal sign up
    @CrossOrigin(origins = "*")
    @PostMapping("/signup/applicant")
    public ResponseEntity<?> signUpApplicant(@RequestBody SignUpRequest signUpRequest ) {
        System.out.println("welcome from applicant signup");
        applicantAuthService.signUp(signUpRequest);
//        userAuthService.createNewUser(signUpRequest);
        ResponseMessage responseMessage = new ResponseMessage("Email confirmation sent. Please verify your email.");
        return ResponseEntity.ok().body(responseMessage);
    }

    @PostMapping("/signup/company")
    public ResponseEntity<?> signUpCompany(@RequestBody SignUpRequest signUpRequest) {
        companyAuthService.signUp(signUpRequest);
        ResponseMessage responseMessage = new ResponseMessage("Email confirmation sent. Please verify your email.");
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
            System.out.println("Verifying email");
            SignUpRequest signUpRequest = jwtUtil.validateSignupToken(token);
            userAuthService.createNewUser(signUpRequest);
            ResponseMessage responseMessage = new ResponseMessage("Email verified successfully. You can now log in.");
            return ResponseEntity.ok().body(responseMessage);
    }

    @PostMapping("/reset-password-request")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        System.out.println("Reset password Request endpoint");
        userAuthService.resetPasswordRequest(resetPasswordRequest.getEmail());
        ResponseMessage responseMessage = new ResponseMessage("Password reset email sent. Please check your email.");
        return  ResponseEntity.ok().body(responseMessage);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("resetToken") String resetToken, @RequestBody ResetPasswordRequest resetPasswordRequest) {
        System.out.println("Reset password endpoint");
        userAuthService.resetPassword(resetToken, resetPasswordRequest.getPassword());
        ResponseMessage responseMessage = new ResponseMessage("Password reset successfully. please login.");
        return  ResponseEntity.ok().body(responseMessage);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        tokenService.deleteCookies(response);
        ResponseMessage responseMessage = new ResponseMessage("Logged out successfully.");
        return ResponseEntity.ok().body(responseMessage);
    }
}

