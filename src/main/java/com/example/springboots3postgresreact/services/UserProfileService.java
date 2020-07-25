package com.example.springboots3postgresreact.services;

import com.example.springboots3postgresreact.datastore.UserProfileDao;
import com.example.springboots3postgresreact.profile.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserProfileService {

    private UserProfileDao userProfileDao;

    @Autowired
    public UserProfileService(@Qualifier("fakeDatabase") UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    public List<UserProfile> getUserProfiles() {
        return this.userProfileDao.getUserProfiles();
    }

    public void uploadUserProfileImage(){
        /*
        TODO: Get file from user
            CHECK:
            1. file is not empty
            2. file is image file
            3. user is in database
            UPDATE:
            5. user profile image link in Database
            UPLOAD:
            6. file to s3
         */
    }

}
