package com.meeting.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.meeting.modules.sys.entity.SysLogEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 * 
 * @author maoxinmin
 */
@Mapper
public interface SysLogDao extends BaseMapper<SysLogEntity> {

}
