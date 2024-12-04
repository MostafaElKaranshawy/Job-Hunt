package com.software.backend.dto;

import com.software.backend.enums.UserType;
import lombok.Data;

@Data
public class SignUpRequest {

    private String email;

    private String password;

    private String firstName; // For applicants

    private String lastName;  // For applicants

    private String companyName; // For companies

    private String googleToken; // For Google OAuth

    private String clientId; // For Google OAuth

    private UserType userType; // Enum: APPLICANT, COMPANY

    // Getters and setters From Lombok
}

