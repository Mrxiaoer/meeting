package com.meeting.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.meeting.common.utils.PageUtils;
import com.meeting.modules.sys.entity.SysLogEntity;

import java.util.Map;

/**
 * 系统日志
 * 
 * @author maoxinmin
 */
public interface SysLogService extends IService<SysLogEntity> {

	PageUtils queryPage(Map<String, Object> params);

}
