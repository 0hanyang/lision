package com.lingxun.smartgunchecksystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lingxun.smartgunchecksystem.dto.DailyCheckCountDto;
import com.lingxun.smartgunchecksystem.dto.UnitCompletionRateDto;
import com.lingxun.smartgunchecksystem.dto.UserCheckStatsDto;
import com.lingxun.smartgunchecksystem.entity.GunCheckRecord;
import com.lingxun.smartgunchecksystem.entity.SysUnit;
import com.lingxun.smartgunchecksystem.entity.SysUser;
import com.lingxun.smartgunchecksystem.mapper.GunCheckRecordMapper;
import com.lingxun.smartgunchecksystem.mapper.SysUnitMapper;
import com.lingxun.smartgunchecksystem.mapper.SysUserMapper;
import com.lingxun.smartgunchecksystem.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger log = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Autowired
    private GunCheckRecordMapper gunCheckRecordMapper;

    @Autowired
    private SysUnitMapper sysUnitMapper;

    @Autowired
    private SysUserMapper sysUserMapper; // To get count of users per unit for completion rate

    @Override
    public List<DailyCheckCountDto> getUnitDailyCheckCountsLast7Days(Long unitId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6); // Last 7 days including today

        LambdaQueryWrapper<GunCheckRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GunCheckRecord::getUnitId, unitId)
               .ge(GunCheckRecord::getCheckTime, startDate.atStartOfDay())
               .le(GunCheckRecord::getCheckTime, endDate.atTime(LocalTime.MAX));

        List<GunCheckRecord> records = gunCheckRecordMapper.selectList(wrapper);

        Map<LocalDate, Long> countsByDate = records.stream()
            .collect(Collectors.groupingBy(record -> record.getCheckTime().toLocalDate(), Collectors.counting()));

        List<DailyCheckCountDto> result = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            result.add(new DailyCheckCountDto(date, countsByDate.getOrDefault(date, 0L)));
        }
        return result;
    }

    @Override
    public List<UnitCompletionRateDto> getMonthlyCompletionRateRanking(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endTime = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        long daysInMonth = yearMonth.lengthOfMonth();

        List<SysUnit> allUnits = sysUnitMapper.selectList(null); // Get all units
        if (allUnits.isEmpty()) {
            return new ArrayList<>();
        }

        List<UnitCompletionRateDto> completionRates = new ArrayList<>();

        for (SysUnit unit : allUnits) {
            // Get total checks for this unit in the month
            LambdaQueryWrapper<GunCheckRecord> recordWrapper = new LambdaQueryWrapper<>();
            recordWrapper.eq(GunCheckRecord::getUnitId, unit.getId())
                         .ge(GunCheckRecord::getCheckTime, startTime)
                         .le(GunCheckRecord::getCheckTime, endTime);
            long actualChecks = gunCheckRecordMapper.selectCount(recordWrapper);

            // Get number of personnel in the unit (assuming 1 check per person per day is target)
            // This logic might be more complex (e.g., only active users, users with guns, etc.)
            LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(SysUser::getUnitId, unit.getId()).eq(SysUser::getEnabled, true);
            long personnelCount = sysUserMapper.selectCount(userWrapper);

            long targetChecks = personnelCount * daysInMonth; // Simplified target
            if (personnelCount == 0) targetChecks = 0; // Avoid division by zero if no personnel

            double completionRate = (targetChecks == 0) ? 0.0 : (double) actualChecks / targetChecks;
            // Cap rate at 1.0 (100%) in case actualChecks > targetChecks for some reason
            completionRate = Math.min(completionRate, 1.0);


            completionRates.add(new UnitCompletionRateDto(
                unit.getId(),
                unit.getUnitName(), // Fetch unit name
                completionRate,
                actualChecks,
                targetChecks
            ));
        }

        // Sort by completion rate descending
        completionRates.sort(Comparator.comparing(UnitCompletionRateDto::getCompletionRate).reversed()
                             .thenComparing(UnitCompletionRateDto::getUnitName)); // Secondary sort by name

        return completionRates;
    }

    @Override
    public UserCheckStatsDto getUserCheckStats(String policeId) {
        // This is similar to GunCheckRecordService.getPoliceCheckHistory but with a simpler DTO.
        // Re-implementing for specific DTO or could call the other service and adapt.
        LambdaQueryWrapper<GunCheckRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GunCheckRecord::getPoliceId, policeId);
        long totalCount = gunCheckRecordMapper.selectCount(wrapper);

        LocalDateTime lastCheckTime = null;
        if (totalCount > 0) {
            LambdaQueryWrapper<GunCheckRecord> lastCheckWrapper = new LambdaQueryWrapper<>();
            lastCheckWrapper.eq(GunCheckRecord::getPoliceId, policeId)
                            .orderByDesc(GunCheckRecord::getCheckTime)
                            .last("LIMIT 1");
            GunCheckRecord lastRecord = gunCheckRecordMapper.selectOne(lastCheckWrapper);
            if (lastRecord != null) {
                lastCheckTime = lastRecord.getCheckTime();
            }
        }
        return new UserCheckStatsDto(policeId, totalCount, lastCheckTime);
    }
}
