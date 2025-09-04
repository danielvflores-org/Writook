package com.danielvflores.writook.model;

public class User {
    private final String username;
    private final String email;
    // FOR SECURITY, THIS PASSWORD WILL BE HASHED BEFORE STORING IT IN A DATABASE.
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

    // ONLY GETTERS ARE NEEDED, SINCE THESE FIELDS DON’T NEED TO BE MODIFIED AFTER CREATING THE OBJECT. A USER IS IMMUTABLE.
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    // IN TO DEVELOPER MODE THIS GETTER IS AVAILABLE, IN PRODUCTION WILL NOT AVAILABLE
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
