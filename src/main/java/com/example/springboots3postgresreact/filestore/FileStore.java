package com.example.springboots3postgresreact.filestore;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sagemaker.model.AmazonSageMakerException;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileStore {

    private final AmazonS3 s3;

    @Autowired
    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void save(String path, String fileName, Optional<Map<String, String>> optionMetadata, InputStream inputStream) {
        ObjectMetadata metadata = new ObjectMetadata();
        optionMetadata.ifPresent((map) -> {
            if (!map.isEmpty()) {
                Objects.requireNonNull(metadata);
                map.forEach(metadata::addUserMetadata);
            }
        });

        try {
            this.s3.putObject(path, fileName, inputStream, metadata);
        } catch (AmazonSageMakerException e) {
            throw new IllegalStateException("Failed to store file in s3", e);
        }
    }
}
