package com.lingxun.smartgunchecksystem.service;

import com.lingxun.smartgunchecksystem.common.enums.AlarmType;
import com.lingxun.smartgunchecksystem.entity.SysUnit; // Assuming SysUnit entity exists
import com.lingxun.smartgunchecksystem.mapper.SysUnitMapper; // To get all units
import com.lingxun.smartgunchecksystem.dto.UnitCheckStatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledTaskService {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTaskService.class);

    @Autowired
    private SysUnitMapper sysUnitMapper; // To fetch all units

    @Autowired
    private GunCheckRecordService gunCheckRecordService; // To check daily status

    @Autowired
    private AlarmLogService alarmLogService;

    // Cron expression for 23:00 every day: "0 0 23 * * ?"
    @Scheduled(cron = "0 0 23 * * ?")
    // @Scheduled(cron = "0 * * * * ?") // For testing: every minute
    public void checkForUncheckedUnits() {
        log.info("Executing daily check for unverified units at {}", LocalDateTime.now());
        LocalDate today = LocalDate.now();

        // 1. Get all operational units (adjust query if there's a status field in SysUnit)
        List<SysUnit> allUnits = sysUnitMapper.selectList(null); // Fetches all units

        if (allUnits == null || allUnits.isEmpty()) {
            log.info("No units found to check.");
            return;
        }

        for (SysUnit unit : allUnits) {
            try {
                UnitCheckStatusDto status = gunCheckRecordService.getUnitDailyCheckStatus(unit.getId(), today);
                if (!status.getHasCompletedChecks()) {
                    String remark = String.format("Unit ID %d (%s) has no gun check records for %s.",
                            unit.getId(), unit.getUnitName(), today.toString());
                    log.warn("ALARM: {}", remark);
                    alarmLogService.createAlarmLog(unit.getId(), AlarmType.UNCHECKED, remark, LocalDateTime.now());
                }
            } catch (Exception e) {
                log.error("Error processing unit ID {}: {}", unit.getId(), e.getMessage(), e);
            }
        }
        log.info("Daily check for unverified units completed at {}", LocalDateTime.now());
    }
}
