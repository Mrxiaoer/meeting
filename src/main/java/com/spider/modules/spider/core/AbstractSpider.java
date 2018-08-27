package com.spider.modules.spider.core;

import com.spider.modules.spider.entity.SpiderClaim;
import com.spider.modules.spider.entity.SpiderRule;

/**
 * spider抽象类
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-25
 */
public abstract class AbstractSpider {

	/**
	 * 线程数
	 */
	private int threadCount = 1;

	int getThreadCount() {
		return threadCount;
	}

	void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	/**
	 * 启动爬虫
	 */
	public abstract void startSpider(int linkId, String url, SpiderClaim spiderClaim, SpiderRule spiderRule);

}
