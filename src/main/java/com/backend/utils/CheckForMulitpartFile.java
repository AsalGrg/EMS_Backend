package com.backend.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class CheckForMulitpartFile implements ConstraintValidator<FileNotEmpty, MultipartFile> {


    private String allowed;

    @Override
    public void initialize(FileNotEmpty constraintAnnotation) {
        allowed = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {

        return value!=null && allowed.equals(value.getContentType());
    }
}
