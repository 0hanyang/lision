package com.lingxun.smartgunchecksystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingxun.smartgunchecksystem.entity.GunCheckRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GunCheckRecordMapper extends BaseMapper<GunCheckRecord> {
    // Custom queries can be added here if needed, though many will be handled by MyBatis Plus wrappers.
}
