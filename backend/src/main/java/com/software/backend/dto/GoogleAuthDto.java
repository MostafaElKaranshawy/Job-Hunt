package com.software.backend.dto;

public class GoogleAuthDto {
    private String idToken;
    private String email;
    private String name;
    private String profilePicture;

    public GoogleAuthDto(String idToken, String email, String name, String profilePicture) {
        this.idToken = idToken;
        this.email = email;
        this.name = name;
        this.profilePicture = profilePicture;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

}
