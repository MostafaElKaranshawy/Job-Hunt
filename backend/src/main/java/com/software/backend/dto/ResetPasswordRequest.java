package com.software.backend.dto;

public class ResetPasswordRequest {
    private String email;

    private String password;
    // Getter and Setter
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
