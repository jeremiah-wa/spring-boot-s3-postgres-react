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

//      1. check file is not empty
        if(file.isEmpty()) throw new IllegalStateException("Can not upload empty file");

//      2. check file is image file
        isImage(file);

//      3. check user is in database
        UserProfile user = getUserProfile(userProfileId);

//      4. check metadata is present
        Map<String, String> metadata = getMetadata(file);

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

    private Map<String, String> getMetadata(MultipartFile file) {
        Map<String,String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private UserProfile getUserProfile(UUID userProfileId) {
        return userProfileDao.getUserProfileById(userProfileId)
                    .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));
    }

    private void isImage(MultipartFile file) {
        if(!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())){
            throw new IllegalStateException("File must be an image [ " +file.getContentType() + " ]");
        }
    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {
        UserProfile user = getUserProfile(userProfileId);
        String path = String.format("%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                user.getUserProfileId());

        return user.getUserProfileImageLink()
                .map(key -> fileStore.download(path, key))
                .orElse(new byte[0]);
    }
}
