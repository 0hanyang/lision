package com.lingxun.smartgunchecksystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lingxun.smartgunchecksystem.dto.*;
import com.lingxun.smartgunchecksystem.entity.GunCheckRecord;
import java.time.LocalDate;

public interface GunCheckRecordService {
    GunCheckRecordDetailsDto addRecord(GunCheckRecordCreateDto createDto);
    IPage<GunCheckRecordDetailsDto> queryRecords(GunCheckRecordQueryDto queryDto);
    PoliceCheckHistoryDto getPoliceCheckHistory(String policeId);
    UnitCheckStatusDto getUnitDailyCheckStatus(Long unitId, LocalDate date);
    GunCheckRecordDetailsDto findById(Long id); // For internal use or direct fetch
    GunCheckRecordDetailsDto updatePhotoUrl(Long recordId, String photoUrl);
}
