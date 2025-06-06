package com.lingxun.smartgunchecksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for a police officer's check history and last check time")
public class PoliceCheckHistoryDto {
    private String policeId;
    private LocalDateTime lastCheckTime;
    private List<GunCheckRecordDetailsDto> history;
    private Long totalChecks;
}
