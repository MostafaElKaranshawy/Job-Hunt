package com.software.backend.model.user;

public class Employee extends User {

    public Employee(String email, String name, String profilePicture, boolean isGoogleUser) {
        super(email, name, profilePicture, isGoogleUser);
    }

    @Override
    public String getRole() {
        return "Employee"; // Role for Employee
    }

    // Add any employee-specific behavior here if necessary
}
