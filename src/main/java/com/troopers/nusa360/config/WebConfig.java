package com.troopers.nusa360.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String directory;

    public WebConfig(@Value("${spring.file.upload.public}") String directory) {
        this.directory = directory;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
                .addResourceHandler("/uploads/public/**")
                .addResourceLocations("file:" + Paths.get(directory).toAbsolutePath().normalize() + "/");

        registry
                .addResourceHandler("/opt/nusa360/uploads/public/**")
                .addResourceLocations("file:" + Paths.get(directory).toAbsolutePath().normalize() + "/");
    }
}
