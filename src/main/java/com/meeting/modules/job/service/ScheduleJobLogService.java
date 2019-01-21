package com.meeting.modules.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.meeting.common.utils.PageUtils;
import com.meeting.modules.job.entity.ScheduleJobLogEntity;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author maoxinmin
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

	PageUtils queryPage(Map<String, Object> params);

}
