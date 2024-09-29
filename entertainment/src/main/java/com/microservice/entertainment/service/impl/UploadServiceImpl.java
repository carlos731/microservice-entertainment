package com.microservice.entertainment.service.impl;

import com.microservice.entertainment.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String upload(MultipartFile file) throws IOException {
        String uploadUrl = "http://localhost:9000/storage/upload-service";
        String apiKey = "asdf1340-90ausd-f8asad0-f9s-df";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);

        // Converter o arquivo MultipartFile para um byte array
        byte[] fileBytes = file.getBytes();
        ByteArrayResource resource = new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        // Criar o corpo da requisição
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> responseEntity = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, Map.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map responseBody = responseEntity.getBody();
                return (String) responseBody.get("url");
            } else {
                String errorMessage = responseEntity.getBody() != null ? responseEntity.getBody().toString() : "Failed to upload file";
                throw new IOException("Upload failed: " + errorMessage);
            }
        } catch (Exception e) {
            throw new IOException("Error during file upload: " + e.getMessage(), e);
        }
    }
}
