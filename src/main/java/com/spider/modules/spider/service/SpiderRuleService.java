package com.spider.modules.spider.service;

import com.spider.modules.spider.entity.SpiderRule;

public interface SpiderRuleService {

	/**
	 * 保存一条
	 *
	 * @param spiderRule
	 */
	void saveOne(SpiderRule spiderRule);

	/**
	 * 根据id查询一条
	 *
	 * @param id
	 * @return SpiderRule
	 */
	SpiderRule getOneById(int id);

}
