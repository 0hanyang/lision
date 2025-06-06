package com.lingxun.smartgunchecksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate; // Added missing import

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for a unit's check status on a specific day")
public class UnitCheckStatusDto {
    private Long unitId;
    private Boolean hasCompletedChecks; // True if any check record exists for the unit on the given day
    private LocalDate checkDate;
}
