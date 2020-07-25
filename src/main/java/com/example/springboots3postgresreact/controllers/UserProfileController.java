package com.example.springboots3postgresreact.controllers;

import com.example.springboots3postgresreact.profile.UserProfile;
import com.example.springboots3postgresreact.services.UserProfileService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"api/v1/user-profile"})
@CrossOrigin("*")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService){
        this.userProfileService = userProfileService;
    }

    public List<UserProfile> getUserProfiles(){
        return userProfileService.getUserProfiles();
    }
}
