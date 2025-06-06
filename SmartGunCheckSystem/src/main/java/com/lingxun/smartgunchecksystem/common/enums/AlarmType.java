package com.lingxun.smartgunchecksystem.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum AlarmType {
    UNCHECKED("未验枪"),
    OVERTIME_CHECK("超时验枪"), // This might require more complex logic if "超时" means late in the day vs. not at all
    MISMATCHED_PERSONNEL_GUN("人员与枪支不符"); // This type of alarm might be generated at the time of check, not via scheduled task

    @EnumValue
    private final String description;

    AlarmType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
