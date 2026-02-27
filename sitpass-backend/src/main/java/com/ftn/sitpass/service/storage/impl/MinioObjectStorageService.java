package com.ftn.sitpass.service.storage.impl;

import com.ftn.sitpass.config.MinioProperties;
import com.ftn.sitpass.exception.InternalServerException;
import com.ftn.sitpass.service.storage.ObjectStorageService;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioObjectStorageService implements ObjectStorageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @PostConstruct
    @Override
    public void ensureBucketExists() {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .build());
            }
        } catch (Exception e) {
            throw new InternalServerException("Unable to initialize MinIO bucket.", e);
        }
    }

    @Override
    public String upload(String objectKey, MultipartFile file) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(objectKey)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return objectKey;
        } catch (Exception e) {
            throw new InternalServerException("Unable to upload object to MinIO.", e);
        }
    }

    @Override
    public byte[] download(String objectKey) {
        try (InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(objectKey)
                .build());
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            inputStream.transferTo(buffer);
            return buffer.toByteArray();
        } catch (Exception e) {
            throw new InternalServerException("Unable to download object from MinIO.", e);
        }
    }

    @Override
    public void remove(String objectKey) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(objectKey)
                    .build());
        } catch (Exception e) {
            throw new InternalServerException("Unable to remove object from MinIO.", e);
        }
    }
}
