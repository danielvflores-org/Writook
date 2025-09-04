package com.danielvflores.writook.model;

public class User {
    private final String username;
    private final String email;
    // FOR SECURITY REASONS, THIS PASSWORD WILL BE HASHED BEFORE BEING STORED IN THE DATABASE.
    private final String password;
    private final String displayName;
    private final String bio;
    private final String profilePictureUrl;

    public User(String username, String email, String password, String displayName, String bio, String profilePictureUrl) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
    }

    // ONLY GETTERS ARE REQUIRED — THE USER OBJECT IS IMMUTABLE.
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    // THIS GETTER IS ONLY AVAILABLE IN DEVELOPER MODE. IT IS DISABLED IN PRODUCTION FOR SECURITY REASONS.
    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }


}
