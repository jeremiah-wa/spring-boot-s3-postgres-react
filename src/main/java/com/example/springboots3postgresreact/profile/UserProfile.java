package com.example.springboots3postgresreact.profile;

import java.util.Optional;
import java.util.UUID;

public class UserProfile {

    private final UUID userProfileId;
    private String username;
    private String userProfileImageLink;


    public UserProfile(UUID userProfileId, String username, String userProfileImageLink) {
        this.userProfileId = userProfileId;
        this.username = username;
        this.userProfileImageLink = userProfileImageLink;
    }

    public UUID getUserProfileId() {
        return this.userProfileId;
    }

    public String getUsername() {
        return username;
    }

    public Optional<String> getUserProfileImageLink() {
        return Optional.ofNullable(this.userProfileImageLink);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserProfileImageLink(String userProfileImageLink) {
        this.userProfileImageLink = userProfileImageLink;
    }

}
