package com.backend.serviceImpls;

import com.backend.configs.CloudinaryConfig;
import com.backend.services.CloudinaryUploadService;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryUploadServiceImplementation implements CloudinaryUploadService {

    @Autowired
    private Cloudinary cloudinary;


    @Override
    public String uploadImage(MultipartFile file) {

        try {
            Map fileData= cloudinary.uploader().upload(file.getBytes(), Map.of());

            //from the map we get the file url where it is stored in the cloud.
            String fileUrl= (String) fileData.get("url");

            return fileUrl;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
