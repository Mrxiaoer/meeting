package com.spider.modules.spider.dao;

import com.spider.modules.spider.entity.TemporaryRecordEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TemporaryRecordDao {

	/**
	 * 插入一条
	 */
	void insertOne(TemporaryRecordEntity temporaryRecord);

	/**
	 * 更新一条
	 */
	int updateOne(TemporaryRecordEntity temporaryRecord);

	/**
	 * 查找一条
	 */
	TemporaryRecordEntity selectOne(TemporaryRecordEntity temporaryRecord);

	/**
	 * 根据linkId查询最新采集的信息（一条）
	 */
	TemporaryRecordEntity queryBylinkId(Integer linkId);

}
