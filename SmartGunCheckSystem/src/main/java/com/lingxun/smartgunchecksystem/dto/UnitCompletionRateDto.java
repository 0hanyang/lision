package com.lingxun.smartgunchecksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for unit's monthly gun check completion rate")
public class UnitCompletionRateDto {
    @Schema(description = "Unit ID")
    private Long unitId;
    @Schema(description = "Unit Name")
    private String unitName; // Added for better readability
    @Schema(description = "Completion rate (e.g., 0.95 for 95%)")
    private Double completionRate; // Assuming rate is checks_done / (total_personnel * days_in_month) or similar logic
    @Schema(description = "Total checks in the month")
    private Long totalChecks;
    @Schema(description = "Target checks for the month (example, can be complex to define)")
    private Long targetChecks;
}
