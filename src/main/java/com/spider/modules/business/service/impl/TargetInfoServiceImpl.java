package com.spider.modules.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.spider.common.utils.Constant;
import com.spider.modules.business.dao.ResultInfoDao;
import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.business.entity.PageInfoEntity;
import com.spider.modules.business.entity.ResultInfoEntity;
import com.spider.modules.business.model.TargetInfoEntity;
import com.spider.modules.business.service.LinkInfoService;
import com.spider.modules.business.service.PageInfoService;
import com.spider.modules.business.service.ResultInfoService;
import com.spider.modules.business.service.TargetInfoService;
import com.spider.modules.spider.config.PhantomJSDriverFactory;
import com.spider.modules.spider.config.PhantomJSDriverPool;
import com.spider.modules.spider.core.HtmlProcess;
import com.spider.modules.spider.core.LoginAnalog;
import com.spider.modules.spider.core.SpiderPage;
import com.spider.modules.spider.dao.AnalogLoginDao;
import com.spider.modules.spider.dao.SpiderRuleDao;
import com.spider.modules.spider.dao.TemporaryRecordDao;
import com.spider.modules.spider.entity.AnalogLoginEntity;
import com.spider.modules.spider.entity.SpiderClaim;
import com.spider.modules.spider.entity.SpiderRule;
import com.spider.modules.spider.entity.TemporaryRecordEntity;
import com.spider.modules.spider.pipeline.SpiderTemporaryRecordPipeline;
import com.spider.modules.spider.service.AnalogLoginService;
import com.spider.modules.spider.service.TemporaryRecordService;
import com.spider.modules.spider.utils.MyStringUtil;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TargetInfoServiceImpl implements TargetInfoService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AnalogLoginDao analogLoginDao;
	@Autowired
	TemporaryRecordDao temporaryRecordDao;
	@Autowired
	AnalogLoginService analogLoginService;
	@Autowired
	LinkInfoService linkInfoService;
	@Autowired
	TemporaryRecordService temporaryRecordService;
	@Autowired
	ResultInfoService resultInfoService;
	@Autowired
	PageInfoService pageInfoService;
	@Autowired
	SpiderRuleDao spiderRuleDao;
	@Autowired
	ResultInfoDao resultInfoDao;
	@Autowired
	SpiderTemporaryRecordPipeline spiderTemporaryRecordPipeline;
	@Autowired
	HtmlProcess htmlprocess;
	@Autowired
	PhantomJSDriverFactory phantomJSDriverFactory;
	@Autowired
	private LoginAnalog loginAnalog;
	@Autowired
	private SpiderPage spiderPage;
	@Autowired
	private PhantomJSDriverPool phantomJSDriverPool;

	/**
	 * 模拟登录返回登录页
	 */
	@Override
	public String tospider(Integer linkId) {

		LinkInfoEntity linkInfo = linkInfoService.queryById(linkId);
		SpiderRule spiderRule = new SpiderRule();
		spiderRule.setIsGetText(false);
		SpiderClaim spiderClaim = new SpiderClaim();
		spiderClaim.setPipeline(spiderTemporaryRecordPipeline);
		spiderPage.startSpider(linkId, linkInfo.getLoginUrl(), spiderClaim, spiderRule);
		TemporaryRecordEntity rc = temporaryRecordService.queryBylinkId(linkId);
		if (MyStringUtil.urlCutParam(rc.getUrl()).equals(MyStringUtil.urlCutParam(linkInfo.getLoginUrl()))) {
			PhantomJSDriver driver = null;
			try {
				driver = phantomJSDriverFactory.create();
			} catch (Exception e) {
				e.printStackTrace();
			}
			spiderClaim.setPhantomJSDriver(driver);
			spiderPage.startSpider(linkId, linkInfo.getLoginUrl(), spiderClaim, spiderRule);
			driver.quit();
			rc = temporaryRecordService.queryBylinkId(linkId);
		}

		return rc.getHtmlFilePath();
	}

	/*模拟登录第2步*/
	@Override
	public LinkInfoEntity update(TargetInfoEntity targetInfo, Integer analogId) throws Exception {
		AnalogLoginEntity analogLogin = new AnalogLoginEntity();
		BeanUtil.copyProperties(targetInfo, analogLogin);
		analogLogin.setId(analogId);
		analogLoginDao.update(analogLogin);
		java.util.Set<Cookie> cookies;
		//模拟浏览器创建连接，发起请求

		PhantomJSDriver driver = phantomJSDriverPool.borrowPhantomJSDriver();
		try {
			cookies = loginAnalog.login(analogLogin.getId(), driver);
			if (cookies != null) {
				LinkInfoEntity linkInfo = new LinkInfoEntity();
				linkInfo.setHasTarget(Constant.VALUE_ONE);
				linkInfo.setLinkId(targetInfo.getLinkId());
				linkInfoService.update(linkInfo);
				return linkInfoService.queryById(targetInfo.getLinkId());
			} else {
				return null;
			}
		} catch (NoSuchMethodException nosuch) {
			logger.info("1、登录信息填写有误,请重新操作!");
			return null;
		} catch (Exception e) {
			logger.info("2、登录信息填写有误,请重新操作!");
			e.printStackTrace();
			return null;
		} finally {
			phantomJSDriverPool.returnObject(driver);
		}
	}

	/*单点采集第一步*/
	@Override
	public TemporaryRecordEntity tothirdspider(Integer linkId,int sleepTime) throws Exception {
		LinkInfoEntity linkInfo = linkInfoService.queryById(linkId);
		AnalogLoginEntity analogLogin = analogLoginService.getOneById(linkInfo.getAnalogId());

		//采集目标页先采用手动输入的cookie进行登录
		SpiderRule spiderRule = new SpiderRule();
		spiderRule.setIsGetText(false);
		PhantomJSDriver driver = phantomJSDriverPool.borrowPhantomJSDriver();
		if (analogLogin.getHandCookie() != null) {
			Set<Cookie> handcookies = MyStringUtil.json2cookie(analogLogin.getHandCookie());
			try {
				SpiderClaim spiderClaim = new SpiderClaim();
				spiderClaim.setPhantomJSDriver(driver);
				spiderClaim.setPipeline(spiderTemporaryRecordPipeline);
				spiderClaim.setCookieSet(handcookies);
				spiderClaim.setSleepTime(sleepTime);
				spiderPage.startSpider(linkId, analogLogin.getTargetUrl(), spiderClaim, spiderRule );
				TemporaryRecordEntity handtem = temporaryRecordService.queryBylinkId(linkId);
				if (StrUtil.isBlank(handtem.getUrl())){
					return new TemporaryRecordEntity();
				}else if (!analogLogin.getTargetUrl().equals(handtem.getUrl())){
					return new TemporaryRecordEntity();
				}else {
					return handtem;
				}
			}finally {
				phantomJSDriverPool.returnObject(driver);
			}
		}else{

			String cookie = analogLogin.getCookie();
			Set<Cookie> cookies = null;
			if (cookie != null) {
				cookies = MyStringUtil.json2cookie(cookie);
			}
			try {
				SpiderClaim spiderClaim = new SpiderClaim();
				spiderClaim.setPhantomJSDriver(driver);
				spiderClaim.setPipeline(spiderTemporaryRecordPipeline);
				spiderClaim.setCookieSet(cookies);
				spiderClaim.setSleepTime(sleepTime);
				spiderPage.startSpider(linkId, analogLogin.getTargetUrl(), spiderClaim, spiderRule);
				String trsUrl = temporaryRecordService.queryBylinkId(linkId).getUrl();
				if (StrUtil.isBlank(trsUrl)) {
					return null;
				} else if (!analogLogin.getTargetUrl().equals(trsUrl)) {
					try {
						spiderClaim.setCookieSet(loginAnalog.login(analogLogin.getId(), driver));
					} catch (NoSuchElementException nse) {
						throw nse;
					}
					spiderPage.startSpider(linkId, analogLogin.getTargetUrl(), spiderClaim, spiderRule);
				}
			} finally {
				phantomJSDriverPool.returnObject(driver);
			}

			//判断目标页是否采集成功
			TemporaryRecordEntity temporaryRecord = temporaryRecordService.queryBylinkId(linkId);
			if (!MyStringUtil.urlCutParam(linkInfo.getUrl()).equals(MyStringUtil.urlCutParam(temporaryRecord.getUrl()))) {
				linkInfo.setFailTimes(linkInfo.getFailTimes() + 1);
				linkInfoService.update(linkInfo);
				if (linkInfo.getFailTimes() % 3 == 0) {
					linkInfo.setHasTarget(Constant.VALUE_ZERO);
					linkInfo.setFailTimes(Constant.VALUE_ZERO);
					linkInfoService.update(linkInfo);
				}
				return null;
			} else {
				if (linkInfo.getFailTimes() != 0) {
					linkInfo.setFailTimes(Constant.VALUE_ZERO);
					linkInfoService.update(linkInfo);
				}
				return temporaryRecord;
			}
		}

	}

	@Override
	@Transactional
	public Map<String, Object> getXpath(Map<String, Object> params) {
		String xpath = (String) params.get("xpath");
		//需要解决//*[@id="spiderContent"] 并非/html/body的问题转化 前端解决

		Integer linkId = (Integer) params.get("linkId");
		LinkInfoEntity linkInfo = linkInfoService.queryById(linkId);

		//xpath插入规则表
		SpiderRule rule = new SpiderRule();
		rule.setXpath(xpath);
		if (rule.getId() != null) {
			rule.setId(null);
		}
		spiderRuleDao.insertOne(rule);

		//更新linkInfo加入规则id
		LinkInfoEntity linkrule = new LinkInfoEntity();
		linkrule.setLinkId(linkId);
		linkrule.setRuleId(rule.getId());
		linkInfoService.update(linkrule);

		//组装解析表头信息
		SpiderRule spiderRule = new SpiderRule();
		spiderRule.setXpath(xpath);
		TemporaryRecordEntity te = new TemporaryRecordEntity();
		te.setLinkId(linkId);
		te.setUrl(linkInfo.getUrl());
		List<String> spiderHead = htmlprocess.process(te, spiderRule);
		ResultInfoEntity resultInfo = new ResultInfoEntity();
		resultInfo.setSystem(linkInfo.getSystem());
		resultInfo.setModule(linkInfo.getModule());
		resultInfo.setCreateTime(new Date());
		resultInfo.setLinkId(linkInfo.getLinkId());
		resultInfoService.save(resultInfo);

		//采集到的表头插入数据库中
		for (String vaule : spiderHead) {
			if (StrUtil.isNotBlank(vaule)) {
				logger.info(vaule);
				PageInfoEntity pageInfo = new PageInfoEntity();
				pageInfo.setNameCn(vaule);
				pageInfo.setResultId(resultInfo.getId());
				pageInfo.setNameEn(MyStringUtil.getPinYinHeadChar(vaule));
				pageInfoService.save(pageInfo);
			}
		}
		//采集的结果返回给前端
		List<PageInfoEntity> pageInfos = pageInfoService.queryByResultId(resultInfo.getId());
		Map<String, Object> map = new HashMap<>();
		map.put("pageInfos", pageInfos);
		return map;
	}

	@Override
	public void updateHead(Map<String, Object> params) {
		String allinformationName = (String) params.get("informationName");
		List<Map<String, Object>> map = (List<Map<String, Object>>) params.get("pageInfos");
		//定义获取pageId
		Integer id = 0;
		for (Map<String, Object> list : map) {
			PageInfoEntity pageInfo = new PageInfoEntity();
			pageInfo.setInformationName(allinformationName);
			pageInfo.setPageId((Integer) list.get("pageId"));
			pageInfo.setState((Integer) list.get("state"));
			pageInfoService.update(pageInfo);
			id = pageInfo.getPageId();
		}
		Integer resultId = pageInfoService.queryById(id).getResultId();
		List<PageInfoEntity> pageInfos = pageInfoService.listByResultId(resultId);
		PageInfoEntity prs = new PageInfoEntity();
		for (PageInfoEntity list : pageInfos) {
			if (list.getInformationName() == null) {
				prs.setPageId(list.getPageId());
				prs.setState(Constant.VALUE_ZERO);
				pageInfoService.update(prs);
			}
		}
	}

}
