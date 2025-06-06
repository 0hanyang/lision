package com.lingxun.smartgunchecksystem.service;

import com.lingxun.smartgunchecksystem.dto.DailyCheckCountDto;
import com.lingxun.smartgunchecksystem.dto.UnitCompletionRateDto;
import com.lingxun.smartgunchecksystem.dto.UserCheckStatsDto;
import java.util.List;

public interface StatisticsService {
    List<DailyCheckCountDto> getUnitDailyCheckCountsLast7Days(Long unitId);
    List<UnitCompletionRateDto> getMonthlyCompletionRateRanking(int year, int month);
    UserCheckStatsDto getUserCheckStats(String policeId);
}
