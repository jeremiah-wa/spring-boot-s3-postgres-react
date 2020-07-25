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
        return 0;
    }

    @Override
    public List<UserProfile> getUserProfiles() {
        return null;
    }

    @Override
    public Optional<UserProfile> getUserProfileById(UUID id) {
        return Optional.empty();
    }
}
