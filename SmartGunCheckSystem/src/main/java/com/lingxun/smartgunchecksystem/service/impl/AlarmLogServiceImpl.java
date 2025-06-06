package com.lingxun.smartgunchecksystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingxun.smartgunchecksystem.dto.AlarmLogDto;
import com.lingxun.smartgunchecksystem.dto.AlarmLogQueryDto;
import com.lingxun.smartgunchecksystem.entity.AlarmLog;
import com.lingxun.smartgunchecksystem.common.enums.AlarmType;
import com.lingxun.smartgunchecksystem.mapper.AlarmLogMapper;
import com.lingxun.smartgunchecksystem.service.AlarmLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class AlarmLogServiceImpl implements AlarmLogService {

    @Autowired
    private AlarmLogMapper alarmLogMapper;

    @Override
    @Transactional
    public AlarmLogDto createAlarmLog(Long unitId, AlarmType alarmType, String remark, LocalDateTime alarmTime) {
        AlarmLog alarmLog = AlarmLog.builder()
                .unitId(unitId)
                .alarmType(alarmType)
                .alarmTime(alarmTime)
                .remark(remark)
                .createTime(LocalDateTime.now()) // Could also be same as alarmTime
                .build();
        alarmLogMapper.insert(alarmLog);
        return convertToDto(alarmLog);
    }

    @Override
    public IPage<AlarmLogDto> queryAlarmLogs(AlarmLogQueryDto queryDto) {
        LambdaQueryWrapper<AlarmLog> wrapper = new LambdaQueryWrapper<>();
        if (queryDto.getUnitId() != null) {
            wrapper.eq(AlarmLog::getUnitId, queryDto.getUnitId());
        }
        if (queryDto.getAlarmType() != null) {
            wrapper.eq(AlarmLog::getAlarmType, queryDto.getAlarmType());
        }
        if (queryDto.getStartDate() != null) {
            wrapper.ge(AlarmLog::getAlarmTime, queryDto.getStartDate().atStartOfDay());
        }
        if (queryDto.getEndDate() != null) {
            wrapper.le(AlarmLog::getAlarmTime, queryDto.getEndDate().atTime(LocalTime.MAX));
        }
        wrapper.orderByDesc(AlarmLog::getAlarmTime);

        Page<AlarmLog> page = new Page<>(queryDto.getPage(), queryDto.getSize());
        IPage<AlarmLog> alarmLogPage = alarmLogMapper.selectPage(page, wrapper);

        return alarmLogPage.convert(this::convertToDto);
    }

    private AlarmLogDto convertToDto(AlarmLog alarmLog) {
        if (alarmLog == null) return null;
        AlarmLogDto dto = new AlarmLogDto();
        BeanUtils.copyProperties(alarmLog, dto);
        return dto;
    }
}
