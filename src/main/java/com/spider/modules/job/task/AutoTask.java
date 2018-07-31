package com.spider.modules.job.task;

import com.spider.modules.spider.core.HtmlProcess;
import com.spider.modules.spider.core.SpiderPage;
import com.spider.modules.spider.entity.AnalogLoginEntity;
import com.spider.modules.spider.entity.SpiderRule;
import com.spider.modules.spider.entity.TemporaryRecordEntity;
import com.spider.modules.spider.pipeline.SpiderTemporaryRecordPipeline;
import com.spider.modules.spider.service.AnalogLoginService;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 自动爬取定时任务
 * <p>
 * autoTask为spring bean的名称
 *
 * @author maoxinmin
 */
@Component("autoTask")
public class AutoTask {
	private final AnalogLoginService analogLoginService;
	private final LoginAnalog loginAnalog;
	private final SpiderTemporaryRecordPipeline spiderTemporaryRecordPipeline;
	private final SpiderPage spiderPage;
	private final HtmlProcess htmlProcess;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	public AutoTask(AnalogLoginService analogLoginService, LoginAnalog loginAnalog, SpiderTemporaryRecordPipeline spiderTemporaryRecordPipeline,
	                SpiderPage spiderPage, HtmlProcess htmlProcess) {
		this.analogLoginService = analogLoginService;
		this.loginAnalog = loginAnalog;
		this.spiderTemporaryRecordPipeline = spiderTemporaryRecordPipeline;
		this.spiderPage = spiderPage;
		this.htmlProcess = htmlProcess;
	}

	public void autoCrawl(String params) throws Exception {
		logger.info("自动爬取方法autoCrawl正在被执行，站点参数为：" + params);

		String isNum = "^[0-9]*$";
		AnalogLoginEntity LoginInfo;
		if (params.matches(isNum)) {
			LoginInfo = analogLoginService.getOneByLinkId(Integer.parseInt(params));
		} else {
			throw new Exception("定时任务参数异常！");
		}
		//模拟登陆，获取cookies
		Set<Cookie> cookies = loginAnalog.login(LoginInfo.getId());
		//通过cookies爬取指定页
		SpiderRule spiderRule1 = new SpiderRule();
		spiderRule1.setIsGetText(false);
		spiderPage.startSpider(Integer.parseInt(params), LoginInfo.getTargetUrl(), false, false, spiderRule1, cookies, spiderTemporaryRecordPipeline);

		//@todo
		SpiderRule spiderRule2 = new SpiderRule();
		//修改了htmlProcess.process(LoginInfo.getTargetUrl(), spiderRule2);  yaonuan0725
		TemporaryRecordEntity temporaryRecord = new TemporaryRecordEntity();
		temporaryRecord.setUrl(LoginInfo.getTargetUrl());
		temporaryRecord.setLinkId(Integer.parseInt(params));
		htmlProcess.process(temporaryRecord, spiderRule2);

	}
}
