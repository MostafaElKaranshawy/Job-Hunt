package com.software.backend.factory;

import com.software.backend.model.user.Employee;
import com.software.backend.model.user.Employer;
import com.software.backend.model.user.User;

public class UserFactory {

    public static User createUser(String role, String email, String name, String profilePicture, boolean isGoogleUser) {
        if (role.equalsIgnoreCase("Employer")) {
            return new Employer(email, name, profilePicture, isGoogleUser);
        } else if (role.equalsIgnoreCase("Employee")) {
            return new Employee(email, name, profilePicture, isGoogleUser);
        }
        throw new IllegalArgumentException("Invalid role specified");
    }
}
