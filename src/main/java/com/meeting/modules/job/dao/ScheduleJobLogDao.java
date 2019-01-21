package com.meeting.modules.job.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.meeting.modules.job.entity.ScheduleJobLogEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 *
 * @author maoxinmin
 */
@Mapper
public interface ScheduleJobLogDao extends BaseMapper<ScheduleJobLogEntity> {

}
