package com.lingxun.smartgunchecksystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("sys_role")
public class SysRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String roleName; // e.g., "市局管理员", "分局管理员", "派出所人员"
    private String roleKey;  // e.g., "admin", "sub_admin", "user"
}
