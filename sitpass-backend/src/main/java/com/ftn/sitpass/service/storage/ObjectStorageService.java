package com.ftn.sitpass.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageService {
    void ensureBucketExists();
    String upload(String objectKey, MultipartFile file);
    byte[] download(String objectKey);
    void remove(String objectKey);
}
