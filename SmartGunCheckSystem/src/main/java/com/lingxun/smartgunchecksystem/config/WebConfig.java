package com.lingxun.smartgunchecksystem.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

    @Value("${file-upload.local.base-path:/data/images}")
    private String localBasePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourceLocation = "file:" + localBasePath + "/"; // Ensure trailing slash
        // This makes files under /data/images/ accessible via /images/**
        // e.g., a file at /data/images/2023/10/pic.jpg becomes accessible at /api/images/2023/10/pic.jpg (if context path is /api)
        // or /images/2023/10/pic.jpg (if no context path)
        // The LocalStorageService returns a URL like /context-path/images/....
        // So the handler should be /images/**
        registry.addResourceHandler("/images/**")
                .addResourceLocations(resourceLocation);
        log.info("Configured static resource handler for /images/** mapping to {}", resourceLocation);
    }
}
