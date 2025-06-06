package com.lingxun.smartgunchecksystem.dto;

import com.lingxun.smartgunchecksystem.common.enums.GunCheckStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime; // Assuming checkTime is sent as LocalDateTime

@Data
@Schema(description = "DTO for data uploaded from Android device")
public class DeviceUploadDto {

    @NotBlank(message = "Gun ID (gunId) cannot be blank")
    @Schema(description = "枪支编号", example = "G12345", requiredMode = Schema.RequiredMode.REQUIRED)
    private String gunId;

    @NotBlank(message = "Police ID (policeId) cannot be blank")
    @Schema(description = "人员编号", example = "P67890", requiredMode = Schema.RequiredMode.REQUIRED)
    private String policeId;

    @NotNull(message = "Check time (checkTime) cannot be null")
    @Schema(description = "验枪时间 (ISO 8601 format, e.g., 2023-10-27T10:15:30)", example = "2023-10-27T10:15:30", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime checkTime;

    @NotBlank(message = "Photo URL (photoUrl) cannot be blank") // Assuming device uploads photo first and sends URL
    @Schema(description = "照片地址", example = "http://example.com/images/some_image.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String photoUrl;

    @NotNull(message = "Status cannot be null")
    @Schema(description = "状态 (NORMAL or ABNORMAL)", example = "NORMAL", requiredMode = Schema.RequiredMode.REQUIRED)
    private GunCheckStatus status;

    @NotNull(message = "Unit ID (unitId) cannot be null")
    @Schema(description = "单位 ID", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long unitId;
}
