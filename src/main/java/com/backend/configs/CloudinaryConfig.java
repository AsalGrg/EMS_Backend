package com.backend.configs;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudConfig(){

        Map<String, String> config = Map.of(
                "cloud_name", "dx6kf6pen",
                "api_key", "956782961796959",
                "api_secret", "yjSKAaNtIVL7fO6nYCT1mTjDrRk"
        );

        return new Cloudinary(config);
    }
}
