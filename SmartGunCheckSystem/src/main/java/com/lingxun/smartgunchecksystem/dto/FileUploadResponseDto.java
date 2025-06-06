package com.lingxun.smartgunchecksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for file upload")
public class FileUploadResponseDto {
    @Schema(description = "URL of the uploaded file", example = "/images/2023-10-28/101/G12345.jpg")
    private String fileUrl;

    @Schema(description = "Original filename", example = "photo.jpg")
    private String originalFilename;

    @Schema(description = "File size in bytes", example = "102400")
    private long size;
}
