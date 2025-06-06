package com.lingxun.smartgunchecksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for daily gun check counts")
public class DailyCheckCountDto {
    @Schema(description = "Date of the count")
    private LocalDate date;
    @Schema(description = "Number of checks on this date")
    private Long count;
}
