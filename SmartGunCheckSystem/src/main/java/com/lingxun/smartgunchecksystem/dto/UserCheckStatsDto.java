package com.lingxun.smartgunchecksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for user's total check count and last check time")
public class UserCheckStatsDto {
    @Schema(description = "User/Police ID")
    private String policeId; // Or userId if linking to SysUser
    @Schema(description = "Total number of gun checks performed by the user")
    private Long totalCheckCount;
    @Schema(description = "Time of the last gun check performed by the user")
    private LocalDateTime lastCheckTime;
}
