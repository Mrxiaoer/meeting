package com.spider.modules.spider.service;

import org.apache.ibatis.annotations.Param;

import com.spider.modules.spider.entity.TemporaryRecordEntity;

public interface TemporaryRecordService {

	/**
	 * 保存一条
	 *
	 * @param temporaryRecord
	 */
	void saveOne(TemporaryRecordEntity temporaryRecord);

	/**
	 * 根据id查询一条
	 *
	 * @param id
	 * @return TemporaryRecordEntity
	 */
	TemporaryRecordEntity getOneById(int id);

	/**
	 * 根据url查询一条
	 *
	 * @param url
	 * @return TemporaryRecordEntity
	 */
	TemporaryRecordEntity getOneByUrl(String url);
	
	/**
                 * 根据linkId查询最新采集的信息（一条）
         * @param linkId
         * @return
         */
        TemporaryRecordEntity queryBylinkId(@Param("linkId") Integer linkId);

}
