package com.software.backend.model.user;

public abstract class User {
    private String email;
    private String name;
    private String profilePicture;
    private boolean isGoogleUser;

    public User(String email, String name, String profilePicture, boolean isGoogleUser) {
        this.email = email;
        this.name = name;
        this.profilePicture = profilePicture;
        this.isGoogleUser = isGoogleUser;
    }

    // Getters and setters for the common attributes
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

    public boolean isGoogleUser() {
        return isGoogleUser;
    }

    public void setGoogleUser(boolean googleUser) {
        isGoogleUser = googleUser;
    }

    public abstract String getRole(); // Subclasses will define this
}
