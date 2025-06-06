package com.lingxun.smartgunchecksystem.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum GunCheckStatus {
    NORMAL("正常"),
    ABNORMAL("异常");

    @EnumValue // Marks this field for MyBatis Plus to persist
    private final String description;

    GunCheckStatus(String description) {
        this.description = description;
    }

    @JsonValue // Used by Jackson to serialize the enum by its description (or code)
    public String getDescription() {
        return description;
    }
}
