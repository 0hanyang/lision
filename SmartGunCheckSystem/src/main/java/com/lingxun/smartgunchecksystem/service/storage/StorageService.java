package com.lingxun.smartgunchecksystem.service.storage;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface StorageService {
    /**
     * Stores a file.
     * @param file The file to store.
     * @param pathPrefix A prefix for the path, e.g., "yyyy-MM-dd/unitId"
     * @param filename The desired filename (without extension, extension will be derived from MultipartFile).
     * @return The publicly accessible URL or path of the stored file.
     * @throws IOException If an error occurs during storage.
     */
    String store(MultipartFile file, String pathPrefix, String filename) throws IOException;
}
