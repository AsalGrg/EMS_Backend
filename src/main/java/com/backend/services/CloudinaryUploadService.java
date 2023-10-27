package com.backend.services;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryUploadService {

    public String uploadImage(MultipartFile file, String folderName);
}
