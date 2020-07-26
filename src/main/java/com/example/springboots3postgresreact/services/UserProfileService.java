package com.example.springboots3postgresreact.services;

import com.example.springboots3postgresreact.bucket.BucketName;
import com.example.springboots3postgresreact.datastore.UserProfileDao;
import com.example.springboots3postgresreact.filestore.FileStore;
import com.example.springboots3postgresreact.profile.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class UserProfileService {

    private final UserProfileDao userProfileDao;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(@Qualifier("fakeDatabase") UserProfileDao userProfileDao, FileStore fileStore) {
        this.userProfileDao = userProfileDao;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getUserProfiles() {
        return this.userProfileDao.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file){
        /*
        TODO: Get file from user
            CHECK:
            1. file is not empty
            2. file is image file
            3. user is in database
            4. metadata is present
            UPDATE:
            5. user profile image link in Database
            UPLOAD:
            6. file to s3
         */

//      1. check file is not empty
        if(file.isEmpty()) throw new IllegalStateException("Can not upload empty file");


//      2. check file is image file
        if(!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())){
            throw new IllegalStateException("File must be an image [ " +file.getContentType() + " ]");
        }

//      3. check user is in database
        UserProfile user = userProfileDao.getUserProfileById(userProfileId)
                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));

//      4. check metadata is present
        Map<String,String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

//      5. update user profile image link in Database
        if (userProfileDao.updateUserProfileById(userProfileId,user)<=0) {
            throw new IllegalStateException("Failed to update database");
        }

//      6. upload file to s3
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            user.setUserProfileImageLink(filename);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
