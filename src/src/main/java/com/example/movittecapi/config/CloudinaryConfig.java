package com.example.movittecapi.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dunlzjre",
                "api_key", "642834974174187",
                "api_secret", "yNNQtgPTOda2ZjWKndTZIQNWEyI"
        ));
    }
}