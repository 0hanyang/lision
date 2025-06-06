package com.lingxun.smartgunchecksystem.dto;

import com.lingxun.smartgunchecksystem.common.enums.AlarmType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "DTO for Alarm Log response")
public class AlarmLogDto {
    private Long id;
    private Long unitId;
    private AlarmType alarmType;
    private LocalDateTime alarmTime;
    private String remark;
    private LocalDateTime createTime;
}
