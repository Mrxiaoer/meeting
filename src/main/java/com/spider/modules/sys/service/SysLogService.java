package com.spider.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.spider.common.utils.PageUtils;
import com.spider.modules.sys.entity.SysLogEntity;

import java.util.Map;

/**
 * 系统日志
 * 
 * @author maoxinmin
 */
public interface SysLogService extends IService<SysLogEntity> {

	PageUtils queryPage(Map<String, Object> params);

}
