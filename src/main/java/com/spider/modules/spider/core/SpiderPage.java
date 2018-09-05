package com.spider.modules.spider.core;

import cn.hutool.core.lang.Assert;
import com.spider.modules.spider.downloader.HttpClientDownloader;
import com.spider.modules.spider.downloader.SeleniumDownloader;
import com.spider.modules.spider.entity.SpiderClaim;
import com.spider.modules.spider.entity.SpiderRule;
import com.spider.modules.spider.pipeline.SpiderContentPipeline;
import com.spider.modules.spider.processor.SpiderPageProcessor;
import java.util.Set;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 爬取页面
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-21
 */
@Service
public class SpiderPage extends AbstractSpider {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpiderPage.class);

	private final SpiderContentPipeline spiderContentPipeline;

	private final SeleniumDownloader seleniumDownloader;

	@Autowired
	public SpiderPage(SpiderContentPipeline spiderContentPipeline, SeleniumDownloader seleniumDownloader) {
		this.spiderContentPipeline = spiderContentPipeline;
		this.seleniumDownloader = seleniumDownloader;
	}

	@Override
	public void startSpider(int linkId, String url, SpiderClaim spiderClaim, SpiderRule spiderRule) {

		Assert.notEmpty(url, "爬取页面URL不能为空");

		if (spiderClaim.getPipeline() == null) {
			spiderClaim.setPipeline(spiderContentPipeline);
		}

		SpiderPageProcessor processor = new SpiderPageProcessor(spiderClaim.isAllDomain(), spiderRule);
		processor.setLinkId(linkId);
		processor.setCookies(spiderClaim.getCookieSet());
		processor.setPhantomJSDriver(spiderClaim.getPhantomJSDriver());
		processor.setSleepTime(spiderClaim.getSleepTime());
		processor.setClickXpathList(spiderClaim.getClickXpathList());
		Spider spider = Spider.create(processor).addUrl(url).addPipeline(spiderClaim.getPipeline());
		if (!spiderClaim.isStaticPage()) {
			spider.setDownloader(seleniumDownloader);
		} else {
			spider.setDownloader(new HttpClientDownloader());
		}
		//开启n个线程抓取
		this.setThreadCount(5);

		long startTime = System.currentTimeMillis();
		spider.thread(this.getThreadCount())
				//启动爬虫
				.run();
		LOGGER.info("爬取页面内容完成：url = {}，耗时{}毫秒", url, System.currentTimeMillis() - startTime);

	}

}
