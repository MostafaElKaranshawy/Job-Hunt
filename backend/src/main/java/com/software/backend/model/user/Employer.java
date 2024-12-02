package com.software.backend.model.user;

public class Employer extends User {

    public Employer(String email, String name, String profilePicture, boolean isGoogleUser) {
        super(email, name, profilePicture, isGoogleUser);
    }

    @Override
    public String getRole() {
        return "Employer"; // Role for Employer
    }

    // Add any employer-specific behavior here if necessary
}
