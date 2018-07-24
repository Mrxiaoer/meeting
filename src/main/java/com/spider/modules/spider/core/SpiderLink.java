package com.spider.modules.spider.core;

import com.spider.modules.spider.downloader.HttpClientDownloader;
import com.spider.modules.spider.downloader.SeleniumDownloader;
import com.spider.modules.spider.entity.SpiderRule;
import com.spider.modules.spider.pipeline.SpiderContentPipeline;
import com.spider.modules.spider.processor.SpiderLinkProcessor;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Set;

/**
 * 爬取链接(未使用)
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-21
 */
@Service
public class SpiderLink extends AbstractSpider {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpiderLink.class);
	private final SeleniumDownloader seleniumDownloader;

	@Autowired
	public SpiderLink(SeleniumDownloader seleniumDownloader) {this.seleniumDownloader = seleniumDownloader;}

	@Override
	public void startSpider(int linkId, String url, boolean allDomain, boolean isStaticPage, SpiderRule spiderRule, Set<Cookie> cookieSet,
	                        Pipeline pipeline) {
		Spider spider = Spider.create(new SpiderLinkProcessor(allDomain)).addUrl(url).addPipeline(new SpiderContentPipeline());
		if (!isStaticPage) {
			spider.setDownloader(seleniumDownloader);
		} else {
			spider.setDownloader(new HttpClientDownloader());
		}
		//开启n个线程抓取
		this.setThreadCount(3);
		spider.thread(this.getThreadCount())
				//启动爬虫
				.run();
		LOGGER.info("爬取页面内链接完成：url = " + url);
	}

}
