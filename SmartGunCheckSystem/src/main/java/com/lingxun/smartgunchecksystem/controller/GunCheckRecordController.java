package com.lingxun.smartgunchecksystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lingxun.smartgunchecksystem.common.response.UnifiedResponse;
import com.lingxun.smartgunchecksystem.dto.*;
import com.lingxun.smartgunchecksystem.service.GunCheckRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/gun-check-records")
@Tag(name = "Gun Check Record Management", description = "APIs for managing gun check records")
public class GunCheckRecordController {

    @Autowired
    private GunCheckRecordService gunCheckRecordService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUB_ADMIN', 'USER')") // Or more specific roles
    @Operation(summary = "Add a new gun check record", description = "Creates a new gun check record. Requires authentication.")
    public ResponseEntity<UnifiedResponse<GunCheckRecordDetailsDto>> addRecord(@Valid @RequestBody GunCheckRecordCreateDto createDto) {
        GunCheckRecordDetailsDto newRecord = gunCheckRecordService.addRecord(createDto);
        return ResponseEntity.ok(UnifiedResponse.success("Record added successfully", newRecord));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUB_ADMIN', 'USER')")
    @Operation(summary = "Query gun check records", description = "Retrieves a paginated list of gun check records based on filters.")
    public ResponseEntity<UnifiedResponse<IPage<GunCheckRecordDetailsDto>>> queryRecords(
        @Valid GunCheckRecordQueryDto queryDto) { // @Valid also works with query params if DTO is annotated
        IPage<GunCheckRecordDetailsDto> records = gunCheckRecordService.queryRecords(queryDto);
        return ResponseEntity.ok(UnifiedResponse.success(records));
    }

    @GetMapping("/police/{policeId}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUB_ADMIN', 'USER')")
    @Operation(summary = "Get police officer's check history", description = "Retrieves all check records and last check time for a specific police officer.")
    public ResponseEntity<UnifiedResponse<PoliceCheckHistoryDto>> getPoliceHistory(
        @Parameter(description = "Police Officer ID", example = "P67890") @PathVariable String policeId) {
        PoliceCheckHistoryDto history = gunCheckRecordService.getPoliceCheckHistory(policeId);
        return ResponseEntity.ok(UnifiedResponse.success(history));
    }

    @GetMapping("/unit/{unitId}/daily-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUB_ADMIN')") // Typically an admin/sub_admin function
    @Operation(summary = "Get unit's daily check status", description = "Checks if a unit has performed any gun checks on a specific day.")
    public ResponseEntity<UnifiedResponse<UnitCheckStatusDto>> getUnitDailyStatus(
        @Parameter(description = "Unit ID", example = "101") @PathVariable Long unitId,
        @Parameter(description = "Date for status check (yyyy-MM-dd)", example = "2023-10-27")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        UnitCheckStatusDto status = gunCheckRecordService.getUnitDailyCheckStatus(unitId, date);
        return ResponseEntity.ok(UnifiedResponse.success(status));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUB_ADMIN', 'USER')")
    @Operation(summary = "Get a specific gun check record by ID")
    public ResponseEntity<UnifiedResponse<GunCheckRecordDetailsDto>> getRecordById(@PathVariable Long id) {
        GunCheckRecordDetailsDto record = gunCheckRecordService.findById(id);
        return ResponseEntity.ok(UnifiedResponse.success(record));
    }
}
