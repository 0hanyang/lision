package com.lingxun.smartgunchecksystem.dto;

import com.lingxun.smartgunchecksystem.common.enums.GunCheckStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "DTO for creating a new Gun Check Record")
public class GunCheckRecordCreateDto {

    @NotBlank(message = "Gun ID cannot be blank")
    @Schema(description = "枪支编号", example = "G12345")
    private String gunId;

    @NotBlank(message = "Police ID cannot be blank")
    @Schema(description = "民警编号", example = "P67890")
    private String policeId;

    @NotNull(message = "Check time cannot be null")
    @Schema(description = "验枪时间", example = "2023-10-27T10:15:30")
    private LocalDateTime checkTime;

    @Schema(description = "图像URL (optional, can be updated later)", example = "http://example.com/img.jpg")
    private String photoUrl;

    @NotNull(message = "Status cannot be null")
    @Schema(description = "是否异常 (NORMAL or ABNORMAL)", example = "NORMAL")
    private GunCheckStatus status;

    @NotNull(message = "Unit ID cannot be null")
    @Schema(description = "单位ID", example = "101")
    private Long unitId;
}
