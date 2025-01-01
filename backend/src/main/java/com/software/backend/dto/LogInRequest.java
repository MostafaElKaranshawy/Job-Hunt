package com.software.backend.dto;

public class LogInRequest {
    private String email;
    private String password;
    private String username;
    // Getter and Setter
    public String getEmail() {
        return email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
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
