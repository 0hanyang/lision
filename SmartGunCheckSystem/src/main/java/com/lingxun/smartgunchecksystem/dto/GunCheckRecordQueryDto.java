package com.lingxun.smartgunchecksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate; // Use LocalDate for date-only filtering for simplicity

@Data
@Schema(description = "DTO for querying Gun Check Records")
public class GunCheckRecordQueryDto {
    @Schema(description = "单位ID for filtering", example = "101")
    private Long unitId;

    @Schema(description = "开始时间 (yyyy-MM-dd) for filtering", example = "2023-01-01")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Schema(description = "结束时间 (yyyy-MM-dd) for filtering (inclusive)", example = "2023-12-31")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @Schema(description = "民警编号 for filtering", example = "P67890")
    private String policeId;

    // Standard pagination params
    @Schema(description = "Page number (1-indexed)", example = "1", defaultValue = "1")
    private int page = 1;

    @Schema(description = "Page size", example = "10", defaultValue = "10")
    private int size = 10;
}
