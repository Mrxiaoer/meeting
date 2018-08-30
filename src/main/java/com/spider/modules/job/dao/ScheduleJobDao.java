package com.spider.modules.job.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.spider.modules.job.entity.ScheduleJobEntity;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 定时任务
 *
 * @author maoxinmin
 */
@Mapper
public interface ScheduleJobDao extends BaseMapper<ScheduleJobEntity> {

	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	/**
	 * 逻辑删除
	 * @param jobIds
	 * @return
	 */
	Integer deleteJobIds(@Param("jobIds")Long[] jobIds);
}
