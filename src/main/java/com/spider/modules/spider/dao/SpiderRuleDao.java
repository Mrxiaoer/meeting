package com.spider.modules.spider.dao;

import com.spider.modules.spider.entity.SpiderRule;
import org.apache.ibatis.annotations.Mapper;

/**
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-18
 */
@Mapper
public interface SpiderRuleDao {

	/**
	 * 插入一条
	 *
	 * @param spiderRule
	 */
	int insertOne(SpiderRule spiderRule);

	/**
	 * 更新一条
	 *
	 * @param spiderRule
	 */
	int updateOne(SpiderRule spiderRule);

	/**
	 * 查找一条
	 *
	 * @param spiderRule
	 * @return
	 */
	SpiderRule selectOne(SpiderRule spiderRule);

}
