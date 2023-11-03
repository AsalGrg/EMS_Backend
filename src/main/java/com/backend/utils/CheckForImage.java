package com.backend.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CheckForImage implements ConstraintValidator<IsImage, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {

        boolean isValid= false;

        List<String> imageContentTypes= List.of(
                "image/apng",
                "image/avif",
                "image/gif",
                "image/jpeg",
                "image/png",
                "image/svg+xml",
                "image/webp"
        );

        if(value!=null && value.getSize()!=0){
            for (String contentType:
                 imageContentTypes) {

                if(value.getContentType().equals(contentType)) isValid=true;
            }
        }

        return isValid;
    }
}
