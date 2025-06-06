package com.lingxun.smartgunchecksystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lingxun.smartgunchecksystem.dto.AlarmLogDto;
import com.lingxun.smartgunchecksystem.dto.AlarmLogQueryDto;
import com.lingxun.smartgunchecksystem.entity.AlarmLog;
import com.lingxun.smartgunchecksystem.common.enums.AlarmType;
import java.time.LocalDateTime;

public interface AlarmLogService {
    AlarmLogDto createAlarmLog(Long unitId, AlarmType alarmType, String remark, LocalDateTime alarmTime);
    IPage<AlarmLogDto> queryAlarmLogs(AlarmLogQueryDto queryDto);
}
