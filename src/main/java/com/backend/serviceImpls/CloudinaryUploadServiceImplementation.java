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
    public String uploadImage(MultipartFile file, String folderName) {

        try {
            //the folder name is the name of the folder where the files are to be saved. Different folders for different type os files such as User Dps, Vendor Documents etc.
            Map fileData= cloudinary.uploader().upload(file.getBytes() , Map.of(
                    "folder", folderName
            ));

            //from the map we get the file url where it is stored in the cloud.
            String fileUrl= (String) fileData.get("url");

            return fileUrl;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
