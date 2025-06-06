package com.lingxun.smartgunchecksystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lingxun.smartgunchecksystem.common.enums.GunCheckStatus;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("gun_check_record")
public class GunCheckRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String gunId;       // 枪支编号
    private String policeId;    // 民警编号
    private LocalDateTime checkTime;  // 验枪时间
    private String photoUrl;    // 图像URL
    private GunCheckStatus status;      // 是否异常 (NORMAL, ABNORMAL)
    private Long unitId;        // 单位ID (foreign key to SysUnit)

    private LocalDateTime createTime; // Record creation time
    private LocalDateTime updateTime; // Record update time
}
