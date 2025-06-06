package com.lingxun.smartgunchecksystem.dto;

import com.lingxun.smartgunchecksystem.common.enums.GunCheckStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Detailed DTO for Gun Check Record response")
public class GunCheckRecordDetailsDto {
    private Long id;
    private String gunId;
    private String policeId;
    private LocalDateTime checkTime;
    private String photoUrl;
    private GunCheckStatus status;
    private Long unitId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
