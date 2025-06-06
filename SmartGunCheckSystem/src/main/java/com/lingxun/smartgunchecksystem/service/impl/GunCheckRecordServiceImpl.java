package com.lingxun.smartgunchecksystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingxun.smartgunchecksystem.dto.*;
import com.lingxun.smartgunchecksystem.entity.GunCheckRecord;
import com.lingxun.smartgunchecksystem.mapper.GunCheckRecordMapper;
import com.lingxun.smartgunchecksystem.service.GunCheckRecordService;
import com.lingxun.smartgunchecksystem.exception.AppException;
import com.lingxun.smartgunchecksystem.common.response.ResponseCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GunCheckRecordServiceImpl implements GunCheckRecordService {

    @Autowired
    private GunCheckRecordMapper gunCheckRecordMapper;

    @Override
    @Transactional
    public GunCheckRecordDetailsDto addRecord(GunCheckRecordCreateDto createDto) {
        GunCheckRecord record = GunCheckRecord.builder()
            .gunId(createDto.getGunId())
            .policeId(createDto.getPoliceId())
            .checkTime(createDto.getCheckTime())
            .photoUrl(createDto.getPhotoUrl())
            .status(createDto.getStatus())
            .unitId(createDto.getUnitId())
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();
        gunCheckRecordMapper.insert(record);
        return convertToDetailsDto(record);
    }

    @Override
    public IPage<GunCheckRecordDetailsDto> queryRecords(GunCheckRecordQueryDto queryDto) {
        LambdaQueryWrapper<GunCheckRecord> wrapper = new LambdaQueryWrapper<>();
        if (queryDto.getUnitId() != null) {
            wrapper.eq(GunCheckRecord::getUnitId, queryDto.getUnitId());
        }
        if (StringUtils.hasText(queryDto.getPoliceId())) {
            wrapper.eq(GunCheckRecord::getPoliceId, queryDto.getPoliceId());
        }
        if (queryDto.getStartDate() != null) {
            wrapper.ge(GunCheckRecord::getCheckTime, queryDto.getStartDate().atStartOfDay());
        }
        if (queryDto.getEndDate() != null) {
            wrapper.le(GunCheckRecord::getCheckTime, queryDto.getEndDate().atTime(LocalTime.MAX));
        }
        wrapper.orderByDesc(GunCheckRecord::getCheckTime); // Default sort

        Page<GunCheckRecord> page = new Page<>(queryDto.getPage(), queryDto.getSize());
        IPage<GunCheckRecord> recordPage = gunCheckRecordMapper.selectPage(page, wrapper);

        return recordPage.convert(this::convertToDetailsDto);
    }

    @Override
    public PoliceCheckHistoryDto getPoliceCheckHistory(String policeId) {
        LambdaQueryWrapper<GunCheckRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GunCheckRecord::getPoliceId, policeId)
               .orderByDesc(GunCheckRecord::getCheckTime);
        List<GunCheckRecord> records = gunCheckRecordMapper.selectList(wrapper);

        LocalDateTime lastCheckTime = null;
        if (!records.isEmpty()) {
            lastCheckTime = records.get(0).getCheckTime();
        }
        List<GunCheckRecordDetailsDto> historyDtos = records.stream()
                                                          .map(this::convertToDetailsDto)
                                                          .collect(Collectors.toList());
        return new PoliceCheckHistoryDto(policeId, lastCheckTime, historyDtos, (long)records.size());
    }

    @Override
    public UnitCheckStatusDto getUnitDailyCheckStatus(Long unitId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        LambdaQueryWrapper<GunCheckRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GunCheckRecord::getUnitId, unitId)
               .ge(GunCheckRecord::getCheckTime, startOfDay)
               .le(GunCheckRecord::getCheckTime, endOfDay);

        boolean completed = gunCheckRecordMapper.selectCount(wrapper) > 0;
        return new UnitCheckStatusDto(unitId, completed, date);
    }

    @Override
    public GunCheckRecordDetailsDto findById(Long id) {
        GunCheckRecord record = gunCheckRecordMapper.selectById(id);
        if (record == null) {
            throw new AppException(ResponseCode.NOT_FOUND, "GunCheckRecord not found with id: " + id);
        }
        return convertToDetailsDto(record);
    }

    @Override
    @Transactional
    public GunCheckRecordDetailsDto updatePhotoUrl(Long recordId, String photoUrl) {
        GunCheckRecord record = gunCheckRecordMapper.selectById(recordId);
        if (record == null) {
            throw new AppException(ResponseCode.NOT_FOUND, "Cannot update photo. GunCheckRecord not found with id: " + recordId);
        }
        record.setPhotoUrl(photoUrl);
        record.setUpdateTime(LocalDateTime.now());
        gunCheckRecordMapper.updateById(record);
        return convertToDetailsDto(record);
    }

    private GunCheckRecordDetailsDto convertToDetailsDto(GunCheckRecord record) {
        if (record == null) return null;
        GunCheckRecordDetailsDto dto = new GunCheckRecordDetailsDto();
        BeanUtils.copyProperties(record, dto);
        return dto;
    }
}
