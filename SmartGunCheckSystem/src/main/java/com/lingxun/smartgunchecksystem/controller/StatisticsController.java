package com.lingxun.smartgunchecksystem.controller;

import com.lingxun.smartgunchecksystem.common.response.UnifiedResponse;
import com.lingxun.smartgunchecksystem.dto.DailyCheckCountDto;
import com.lingxun.smartgunchecksystem.dto.UnitCompletionRateDto;
import com.lingxun.smartgunchecksystem.dto.UserCheckStatsDto;
import com.lingxun.smartgunchecksystem.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@Tag(name = "Statistics & Visualization", description = "APIs for statistical data")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/unit/{unitId}/daily-checks-last-7days")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUB_ADMIN')")
    @Operation(summary = "Get unit's daily check counts for the last 7 days")
    public ResponseEntity<UnifiedResponse<List<DailyCheckCountDto>>> getUnitDailyChecksLast7Days(
            @Parameter(description = "Unit ID", example = "101") @PathVariable Long unitId) {
        List<DailyCheckCountDto> data = statisticsService.getUnitDailyCheckCountsLast7Days(unitId);
        return ResponseEntity.ok(UnifiedResponse.success(data));
    }

    @GetMapping("/completion-rate-ranking")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUB_ADMIN')")
    @Operation(summary = "Get monthly completion rate ranking for all units")
    public ResponseEntity<UnifiedResponse<List<UnitCompletionRateDto>>> getMonthlyCompletionRateRanking(
            @Parameter(description = "Year", example = "2023") @RequestParam int year,
            @Parameter(description = "Month (1-12)", example = "10") @RequestParam int month) {
        List<UnitCompletionRateDto> data = statisticsService.getMonthlyCompletionRateRanking(year, month);
        return ResponseEntity.ok(UnifiedResponse.success(data));
    }

    @GetMapping("/user/{policeId}/check-stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUB_ADMIN', 'USER')") // User can see their own stats
    @Operation(summary = "Get user's total check count and last check time")
    public ResponseEntity<UnifiedResponse<UserCheckStatsDto>> getUserCheckStats(
            @Parameter(description = "Police ID", example = "P67890") @PathVariable String policeId) {
        // Add security check: if role is USER, policeId must match logged-in user's policeId
        // This requires getting current user's details. For now, relies on PreAuthorize or higher level checks.
        UserCheckStatsDto data = statisticsService.getUserCheckStats(policeId);
        return ResponseEntity.ok(UnifiedResponse.success(data));
    }
}
