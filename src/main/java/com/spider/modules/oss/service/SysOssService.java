package com.spider.modules.oss.service;

import com.baomidou.mybatisplus.service.IService;
import com.spider.common.utils.PageUtils;
import com.spider.modules.oss.entity.SysOssEntity;

import java.util.Map;

/**
 * 文件上传
 * 
 * @author maoxinmin
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);
}
