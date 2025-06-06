package com.lingxun.smartgunchecksystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lingxun.smartgunchecksystem.common.enums.AlarmType;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("alarm_log")
public class AlarmLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long unitId;        // 单位ID (Foreign key to SysUnit)
    private AlarmType alarmType;    // 预警类型
    private LocalDateTime alarmTime;  // 预警发生时间 (when the log is created)
    private String remark;          // 备注 (e.g., "Unit X did not perform gun checks on YYYY-MM-DD")

    private LocalDateTime createTime; // Record creation time (same as alarmTime for scheduled alarms)
}
