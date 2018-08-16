package com.spider.modules.spider.service.impl;

import com.spider.modules.spider.dao.SpiderRuleDao;
import com.spider.modules.spider.entity.SpiderRule;
import com.spider.modules.spider.service.SpiderRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 爬取规则实现类
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-19
 */
@Service
public class SpiderRuleServiceImpl implements SpiderRuleService {

	@Autowired
	private SpiderRuleDao spiderRuleDao;

	@Override
	public void saveOne(SpiderRule spiderRule) {
		int updateNum = spiderRuleDao.updateOne(spiderRule);
		if (updateNum < 1) {
			spiderRuleDao.insertOne(spiderRule);
		}
	}

	@Override
	public SpiderRule getOneById(int id) {
		SpiderRule sr = new SpiderRule();
		sr.setId(id);
		return spiderRuleDao.selectOne(sr);
	}

}
