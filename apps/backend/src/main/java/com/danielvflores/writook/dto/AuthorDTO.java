package com.danielvflores.writook.dto;

import com.danielvflores.writook.model.User;

public class AuthorDTO {
    private String username;
    private String email;
    private String displayName;
    private String bio;
    private String profilePictureUrl;

    public AuthorDTO() {}

    public AuthorDTO(String username, String email, String displayName, String bio, String profilePictureUrl) {
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
    }

    public AuthorDTO(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.displayName = user.getDisplayName();
        this.bio = user.getBio();
        this.profilePictureUrl = user.getProfilePictureUrl();
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
}