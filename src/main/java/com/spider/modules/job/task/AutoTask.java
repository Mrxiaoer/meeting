package com.spider.modules.job.task;

import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.business.entity.PageInfoEntity;
import com.spider.modules.business.entity.ResultInfoEntity;
import com.spider.modules.business.service.LinkInfoService;
import com.spider.modules.business.service.PageInfoService;
import com.spider.modules.business.service.ResultInfoService;
import com.spider.modules.spider.core.HtmlProcess;
import com.spider.modules.spider.core.LoginAnalog;
import com.spider.modules.spider.core.SpiderPage;
import com.spider.modules.spider.entity.AnalogLoginEntity;
import com.spider.modules.spider.entity.SpiderRule;
import com.spider.modules.spider.entity.TemporaryRecordEntity;
import com.spider.modules.spider.pipeline.SpiderTemporaryRecordPipeline;
import com.spider.modules.spider.service.AnalogLoginService;
import com.spider.modules.spider.service.SpiderRuleService;
import com.spider.modules.spider.service.TemporaryRecordService;
import com.spider.modules.spider.utils.MyStringUtil;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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
	TemporaryRecordService temporaryRecordService;

	@Autowired
	LinkInfoService linkInfoService;

	@Autowired
	SpiderRuleService spiderRuleService;

	@Autowired
	HtmlProcess htmlprocess;

	@Autowired
	ResultInfoService resultInfoService;

	@Autowired
	PageInfoService pageInfoService;

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
		//根据linkId(params)查询analog_login表对应的信息
		if (params.matches(isNum)) {
			LoginInfo = analogLoginService.getOneByLinkId(Integer.parseInt(params));
		} else {
			throw new Exception("定时任务参数异常！");
		}

		Integer linkId  = Integer.parseInt(params);
		SpiderRule spiderRule = new SpiderRule();
		spiderRule.setIsGetText(false);
		//模拟登录
		Set<Cookie> cookies = null;
		//模拟登录，获取cookie
		try {
			cookies =  loginAnalog.login(LoginInfo.getId());
		}catch (Exception e){
			e.printStackTrace();
		}
		//获取目标页
		spiderPage.startSpider(linkId, LoginInfo.getTargetUrl(), false, false, spiderRule, cookies, spiderTemporaryRecordPipeline);
		//取得目标页
		Map<String,Object> map = new HashMap<String, Object>();
		LinkInfoEntity spiderLink = linkInfoService.queryById(linkId);
		SpiderRule rule = spiderRuleService.getOneById(spiderLink.getRuleId());


		//组装解析表头信息
		TemporaryRecordEntity te = new TemporaryRecordEntity();
		te.setLinkId(linkId);
		te.setUrl(spiderLink.getUrl());
		List<String> spiderHead = htmlprocess.process(te, rule);
		ResultInfoEntity resultInfo = new ResultInfoEntity();
		resultInfo.setSystem(spiderLink.getSystem());
		resultInfo.setModule(spiderLink.getModule());
		resultInfo.setCreateTime(new Date());
		resultInfo.setLinkId(spiderLink.getLinkId());
		resultInfoService.save(resultInfo);

		//采集到的表头插入数据库中
		for(String vaule:spiderHead) {
			PageInfoEntity pageInfo = new PageInfoEntity();
			pageInfo.setNameCn(vaule);
			pageInfo.setResultId(resultInfo.getId());
			MyStringUtil myStringUtil = new MyStringUtil();
			pageInfo.setNameEn(myStringUtil.getPinYinHeadChar(vaule));
			pageInfoService.save(pageInfo);
		}



//		//@todo
//		SpiderRule spiderRule2 = new SpiderRule();
//		//修改了htmlProcess.process(LoginInfo.getTargetUrl(), spiderRule2);  yaonuan0725
//		TemporaryRecordEntity temporaryRecord = new TemporaryRecordEntity();
//		temporaryRecord.setUrl(LoginInfo.getTargetUrl());
//		temporaryRecord.setLinkId(Integer.parseInt(params));
//		htmlProcess.process(temporaryRecord, spiderRule2);

	}
}
