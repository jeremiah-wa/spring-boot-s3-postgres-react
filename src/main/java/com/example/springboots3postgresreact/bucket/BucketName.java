package com.example.springboots3postgresreact.bucket;

public enum BucketName {
    PROFILE_IMAGE("aws-image-upload-app");

    private final String bucketName;

    private BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return this.bucketName;
    }
}
