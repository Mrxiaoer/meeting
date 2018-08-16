package com.spider.modules.spider.service;

import com.spider.modules.spider.entity.SpiderRule;

public interface SpiderRuleService {

	/**
	 * 保存一条
	 */
	void saveOne(SpiderRule spiderRule);

	/**
	 * 根据id查询一条
	 *
	 * @return SpiderRule
	 */
	SpiderRule getOneById(int id);

}
