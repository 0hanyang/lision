package com.lingxun.smartgunchecksystem.controller;

import com.lingxun.smartgunchecksystem.common.response.UnifiedResponse;
import com.lingxun.smartgunchecksystem.dto.DeviceUploadDto;
import com.lingxun.smartgunchecksystem.dto.GunCheckRecordCreateDto;
import com.lingxun.smartgunchecksystem.dto.GunCheckRecordDetailsDto;
import com.lingxun.smartgunchecksystem.service.GunCheckRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/device")
@Tag(name = "Device Interface", description = "APIs for Android device communication")
public class DeviceController {

    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private GunCheckRecordService gunCheckRecordService;

    @PostMapping("/uploadRecord")
    @Operation(summary = "Upload Gun Check Record from Device",
               description = "Endpoint for Android smart gun barrel to upload gun check information. " +
                             "This endpoint is expected to be publicly accessible or use device-specific auth.")
    public ResponseEntity<UnifiedResponse<GunCheckRecordDetailsDto>> uploadRecord(
            @Valid @RequestBody DeviceUploadDto deviceUploadDto) {

        log.info("Received gun check record upload from device: {}", deviceUploadDto);

        // Convert DeviceUploadDto to GunCheckRecordCreateDto as service expects that
        // This also acts as a mapping layer if the DTOs diverge more in the future
        GunCheckRecordCreateDto createDto = new GunCheckRecordCreateDto();
        createDto.setGunId(deviceUploadDto.getGunId());
        createDto.setPoliceId(deviceUploadDto.getPoliceId());
        createDto.setCheckTime(deviceUploadDto.getCheckTime());
        createDto.setPhotoUrl(deviceUploadDto.getPhotoUrl()); // Assuming photoUrl is directly provided
        createDto.setStatus(deviceUploadDto.getStatus());
        createDto.setUnitId(deviceUploadDto.getUnitId());

        try {
            GunCheckRecordDetailsDto savedRecord = gunCheckRecordService.addRecord(createDto);
            log.info("Device upload processed successfully. Record ID: {}", savedRecord.getId());
            return ResponseEntity.ok(UnifiedResponse.success("Record uploaded successfully", savedRecord));
        } catch (Exception e) {
            log.error("Error processing device upload: {}", e.getMessage(), e);
            // Consider returning a more specific error response if needed
            return ResponseEntity.internalServerError().body(UnifiedResponse.error(500, "Failed to process upload: " + e.getMessage()));
        }
    }
}
