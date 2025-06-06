package com.lingxun.smartgunchecksystem.service.storage;

import com.lingxun.smartgunchecksystem.exception.AppException;
import com.lingxun.smartgunchecksystem.common.response.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service("localStorageService") // Named for potential multiple storage service implementations
public class LocalStorageService implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(LocalStorageService.class);

    private final Path rootLocation;
    private final String baseUrlPath; // Used to construct the return URL

    public LocalStorageService(@Value("${file-upload.local.base-path:/data/images}") String localBasePath,
                               @Value("${server.servlet.context-path:}") String contextPath) {
        // Ensure localBasePath is absolute or resolve it.
        // For simplicity, assuming it's an absolute path from config.
        this.rootLocation = Paths.get(localBasePath);
        // Base URL path for accessing images, e.g., /api/images if contextPath is /api
        // Or just /images if served directly.
        // This needs to align with how static resources are served.
        this.baseUrlPath = contextPath + "/images"; // Assuming files will be served from /images endpoint or static mapping
        try {
            Files.createDirectories(rootLocation);
            log.info("Local storage directory created/ensured at: {}", rootLocation.toAbsolutePath());
        } catch (IOException e) {
            log.error("Could not initialize storage location", e);
            throw new AppException(ResponseCode.INTERNAL_SERVER_ERROR, "Could not initialize storage location: " + e.getMessage());
        }
    }

    @Override
    public String store(MultipartFile file, String pathPrefix, String baseFilename) throws IOException {
        if (file.isEmpty()) {
            throw new AppException(ResponseCode.BAD_REQUEST, "Failed to store empty file.");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(originalFilename);
        if (extension == null || extension.isEmpty()) {
            // Fallback or determine from content type if necessary
            extension = "jpg";
        }

        String finalFilename = baseFilename + "." + extension;

        Path destinationDirectory = this.rootLocation.resolve(pathPrefix);
        Files.createDirectories(destinationDirectory); // Ensure directory exists

        Path destinationFile = destinationDirectory.resolve(finalFilename)
                                   .normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(destinationDirectory.toAbsolutePath())) {
            // This is a security check
            throw new AppException(ResponseCode.BAD_REQUEST, "Cannot store file outside current directory.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            log.info("Stored file {} to {}", finalFilename, destinationFile);
        }

        // Return the relative path that can be used to form a URL
        // e.g., /images/2023-10-28/101/G12345.jpg
        return Paths.get(baseUrlPath, pathPrefix, finalFilename).toString().replace("\\", "/");
    }
}
