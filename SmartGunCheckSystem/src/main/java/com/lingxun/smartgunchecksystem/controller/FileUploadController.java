package com.lingxun.smartgunchecksystem.controller;

import com.lingxun.smartgunchecksystem.common.response.UnifiedResponse;
import com.lingxun.smartgunchecksystem.dto.FileUploadResponseDto;
import com.lingxun.smartgunchecksystem.service.GunCheckRecordService;
import com.lingxun.smartgunchecksystem.service.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/upload")
@Tag(name = "File Upload", description = "APIs for uploading files (e.g., images)")
public class FileUploadController {

    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    private final StorageService storageService;
    private final GunCheckRecordService gunCheckRecordService;

    @Value("${file-upload.storage-type:local}") // Default to local
    private String storageType;

    @Autowired
    public FileUploadController(@Qualifier("localStorageService") StorageService localStorageService,
                                @Qualifier("ossStorageService") StorageService ossStorageService,
                                @Value("${file-upload.storage-type:local}") String activeStorageType,
                                GunCheckRecordService gunCheckRecordService) {
        if ("oss".equalsIgnoreCase(activeStorageType)) {
            this.storageService = ossStorageService;
            log.info("File upload configured to use OSS.");
        } else {
            this.storageService = localStorageService;
            log.info("File upload configured to use Local Storage.");
        }
        this.gunCheckRecordService = gunCheckRecordService;
    }

    @PostMapping("/image")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUB_ADMIN', 'USER')") // Or specific roles that can upload
    @Operation(summary = "Upload an image",
               description = "Uploads an image file. Requires `gunCheckRecordId` to link the image. " +
                             "Optionally, `unitId` and `gunId` can be provided to structure the storage path.")
    public ResponseEntity<UnifiedResponse<FileUploadResponseDto>> uploadImage(
            @Parameter(description = "Image file to upload", required = true) @RequestParam("file") MultipartFile file,
            @Parameter(description = "Gun Check Record ID to associate this image with", required = true) @RequestParam("gunCheckRecordId") Long gunCheckRecordId,
            @Parameter(description = "Unit ID (used for path structuring)", required = true) @RequestParam("unitId") String unitId,
            @Parameter(description = "Gun ID (used as part of filename)", required = true) @RequestParam("gunId") String gunId
            ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(UnifiedResponse.error(400, "File cannot be empty."));
        }

        // Path structure: yyyy-MM-dd/unitId/gunId.jpg
        String datePart = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String pathPrefix = datePart + "/" + unitId; // e.g., 2023-10-28/101
        // Filename will be gunId (extension added by service)

        try {
            String fileUrl = storageService.store(file, pathPrefix, gunId);

            // Automatically write back to the gun check record table
            gunCheckRecordService.updatePhotoUrl(gunCheckRecordId, fileUrl);
            log.info("Image uploaded successfully for record ID {}. URL: {}", gunCheckRecordId, fileUrl);

            FileUploadResponseDto responseDto = new FileUploadResponseDto(fileUrl, file.getOriginalFilename(), file.getSize());
            return ResponseEntity.ok(UnifiedResponse.success("File uploaded successfully and linked to record.", responseDto));
        } catch (IOException e) {
            log.error("Failed to upload file: " + file.getOriginalFilename(), e);
            return ResponseEntity.internalServerError().body(UnifiedResponse.error(500, "Failed to upload file: " + e.getMessage()));
        } catch (Exception e) { // Catch other exceptions like from updatePhotoUrl
            log.error("Error processing file upload or updating record: {}", e.getMessage(), e);
             return ResponseEntity.internalServerError().body(UnifiedResponse.error(500, "Error processing request: " + e.getMessage()));
        }
    }
}
