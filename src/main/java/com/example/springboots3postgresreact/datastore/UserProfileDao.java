package com.example.springboots3postgresreact.datastore;

import com.example.springboots3postgresreact.profile.UserProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileDao {

    int insertUserProfile(UserProfile userProfile);
    default int insertUserProfile(String username){
        return insertUserProfile(new UserProfile(UUID.randomUUID(), username, null));
    }
    List<UserProfile> getUserProfiles();
    Optional<UserProfile> getUserProfileById(UUID id);
}
