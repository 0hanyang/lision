package com.lingxun.smartgunchecksystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lingxun.smartgunchecksystem.common.response.UnifiedResponse;
import com.lingxun.smartgunchecksystem.dto.AlarmLogDto;
import com.lingxun.smartgunchecksystem.dto.AlarmLogQueryDto;
import com.lingxun.smartgunchecksystem.service.AlarmLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alarm-logs")
@Tag(name = "Alarm Log Management", description = "APIs for querying alarm logs")
public class AlarmLogController {

    @Autowired
    private AlarmLogService alarmLogService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUB_ADMIN')") // Only admins should see alarms
    @Operation(summary = "Query alarm logs", description = "Retrieves a paginated list of alarm logs based on filters.")
    public ResponseEntity<UnifiedResponse<IPage<AlarmLogDto>>> queryAlarmLogs(
        @Valid AlarmLogQueryDto queryDto) {
        IPage<AlarmLogDto> alarmLogs = alarmLogService.queryAlarmLogs(queryDto);
        return ResponseEntity.ok(UnifiedResponse.success(alarmLogs));
    }
}
