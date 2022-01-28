package com.example.springboots3postgresreact.datastore;

import com.example.springboots3postgresreact.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeDatabase")
public class UserProfileDataStore implements UserProfileDao{

    private static final List<UserProfile> DB = new ArrayList<>();

    static {
        DB.add(new UserProfile(UUID.randomUUID(), "Sir Tom Jones", null));
    }

    @Override
    public int insertUserProfile(UserProfile userProfile) {
        return DB.add(userProfile)? 1 : 0;
    }

    @Override
    public List<UserProfile> getUserProfiles() {
        return DB;
    }

    @Override
    public Optional<UserProfile> getUserProfileById(UUID userProfileId) {
        return DB.stream()
                .filter(user -> user.getUserProfileId().equals(userProfileId))
                .findFirst();
    }

    @Override
    public int updateUserProfileById(UUID userProfileId, UserProfile userProfile) {
        return getUserProfileById(userProfileId)
                .map(user -> {
                    int indexOfUserToDelete = DB.indexOf(userProfile);
                    if (indexOfUserToDelete >= 0) {
                        DB.set(indexOfUserToDelete, userProfile);
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }
}
