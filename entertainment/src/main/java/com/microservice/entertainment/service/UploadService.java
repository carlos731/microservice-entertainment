package com.microservice.entertainment.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    public String upload(MultipartFile file) throws IOException;
}
