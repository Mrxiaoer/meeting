package com.spider.modules.spider.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 链接处理器
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-24
 */
public class SpiderLinkProcessor implements PageProcessor {

	// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site;

	//是否全站
	private boolean allDomain;

	public SpiderLinkProcessor(boolean allDomain) {
		this.site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(5000);
		this.allDomain = allDomain;
	}

	public boolean isAllDomain() {
		return allDomain;
	}

	public void setAllDomain(boolean allDomain) {
		this.allDomain = allDomain;
	}

	// process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
	@Override
	public void process(Page page) {
		// 部分二：定义如何抽取页面信息，并保存下来
		page.putField("links", page.getHtml().links().all());
		if (page.getResultItems().get("links") == null) {
			//skip this page
			page.setSkip(true);
		}

		if (this.allDomain) {
			String domain = site.getDomain();
			// 部分三：从页面发现后续的url地址来抓取
			page.addTargetRequests(page.getHtml().links().regex("(https?://" + domain + "/[\\w\\-\\/]*)").all());
		}
	}

	@Override
	public Site getSite() {
		return site;
	}

}
