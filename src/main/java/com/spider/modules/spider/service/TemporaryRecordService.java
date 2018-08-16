package com.spider.modules.spider.service;

import com.spider.modules.spider.entity.TemporaryRecordEntity;
import org.apache.ibatis.annotations.Param;

public interface TemporaryRecordService {

	/**
	 * 保存一条
	 */
	void saveOne(TemporaryRecordEntity temporaryRecord);

	/**
	 * 根据id查询一条
	 *
	 * @return TemporaryRecordEntity
	 */
	TemporaryRecordEntity getOneById(int id);

	/**
	 * 根据url查询一条
	 *
	 * @return TemporaryRecordEntity
	 */
	TemporaryRecordEntity getOneByUrl(String url);

	/**
	 * 根据linkId查询最新采集的信息（一条）
	 */
	TemporaryRecordEntity queryBylinkId(@Param("linkId") Integer linkId);

}
