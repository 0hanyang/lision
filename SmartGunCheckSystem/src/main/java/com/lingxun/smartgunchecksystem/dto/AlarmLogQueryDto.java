package com.lingxun.smartgunchecksystem.dto;

import com.lingxun.smartgunchecksystem.common.enums.AlarmType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Schema(description = "DTO for querying Alarm Logs")
public class AlarmLogQueryDto {
    @Schema(description = "单位ID for filtering", example = "101")
    private Long unitId;

    @Schema(description = "预警类型 for filtering", example = "UNCHECKED")
    private AlarmType alarmType;

    @Schema(description = "开始日期 (yyyy-MM-dd) for filtering by alarmTime", example = "2023-01-01")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Schema(description = "结束日期 (yyyy-MM-dd) for filtering by alarmTime (inclusive)", example = "2023-12-31")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @Schema(description = "Page number (1-indexed)", example = "1", defaultValue = "1")
    private int page = 1;

    @Schema(description = "Page size", example = "10", defaultValue = "10")
    private int size = 10;
}
