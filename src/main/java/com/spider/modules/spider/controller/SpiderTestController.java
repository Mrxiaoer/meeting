package com.spider.modules.spider.controller;

import cn.hutool.json.JSONUtil;
import com.spider.modules.spider.config.PhantomJSDriverPool;
import com.spider.modules.spider.config.SpiderConstant;
import com.spider.modules.spider.core.LoginAnalog;
import com.spider.modules.spider.core.SpiderPage;
import com.spider.modules.spider.entity.AnalogLoginEntity;
import com.spider.modules.spider.entity.ChaoJiYingResult;
import com.spider.modules.spider.entity.SpiderRule;
import com.spider.modules.spider.pipeline.SpiderContentPipeline;
import com.spider.modules.spider.pipeline.SpiderTemporaryRecordPipeline;
import com.spider.modules.spider.service.AnalogLoginService;
import com.spider.modules.spider.service.TemporaryRecordService;
import com.spider.modules.spider.utils.ChaoJiYing;
import com.spider.modules.spider.utils.JieTu;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.utils.UrlUtils;

import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 测试
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-30
 */
@RestController
@RequestMapping("spider")
@Scope("session")
public class SpiderTestController {

	private final AnalogLoginService analogLoginService;
	private final LoginAnalog loginAnalog;
	private final SpiderPage spiderPage;
	private final JieTu jieTu;
	private final SpiderTemporaryRecordPipeline spiderTemporaryRecordPipeline;
	private final TemporaryRecordService temporaryRecordService;
	private Logger logger = LoggerFactory.getLogger(SpiderTestController.class);

	@Autowired
	PhantomJSDriverPool phantomJSDriverPool;

	@Autowired
	public SpiderTestController(AnalogLoginService analogLoginService, LoginAnalog loginAnalog, SpiderPage spiderPage, JieTu jieTu,
	                            SpiderTemporaryRecordPipeline spiderTemporaryRecordPipeline, TemporaryRecordService temporaryRecordService) {
		this.analogLoginService = analogLoginService;
		this.loginAnalog = loginAnalog;
		this.spiderPage = spiderPage;
		this.jieTu = jieTu;
		this.spiderTemporaryRecordPipeline = spiderTemporaryRecordPipeline;
		this.temporaryRecordService = temporaryRecordService;
	}

	@RequestMapping("/allProcess")
	public void allProcess(String url, String loginButtonXpath, String usernameXpath, String passwordXpath, String verifycodeXpath,
	                       String verifyCodeUrl, String username, String password) {

		Set<Cookie> cookies = this.tryLogin(url, loginButtonXpath, usernameXpath, passwordXpath, verifycodeXpath, verifyCodeUrl, username, password);

		this.getPageByCookie(url, cookies);
		System.out.println(phantomJSDriverPool.listAllObjects());
		System.out.println(temporaryRecordService.getOneByUrl(url));
	}

	private void getPageByCookie(String url, Set<Cookie> cookies) {

		SpiderRule spiderRule = new SpiderRule();
		spiderRule.setIsGetText(false);

		spiderPage.startSpider(10086, url, false, false, spiderRule, cookies, spiderTemporaryRecordPipeline);

	}

	@RequestMapping("/getPage")
	public void getPage(String url, @RequestParam(required = false) int loginAnalogId) {

		SpiderRule spiderRule = new SpiderRule();
		spiderRule.setIsGetText(false);
		analogLoginService.getOneById(loginAnalogId);

		spiderPage.startSpider(10088, url, false, false, spiderRule, null, spiderTemporaryRecordPipeline);

	}

	@RequestMapping("/tryLogin")
	public Set<Cookie> tryLogin(String loginUrl, String loginButtonXpath, String usernameXpath, String passwordXpath, String verifycodeXpath,
	                            String verifyCodeUrl, String username, String password) {

		int id = this.saveLoginInfo(loginUrl, loginButtonXpath, usernameXpath, passwordXpath, verifycodeXpath, verifyCodeUrl, username, password);

		return this.loginAnalog(id);
	}

	@RequestMapping("/saveLoginInfo")
	public int saveLoginInfo(String loginUrl, String loginButtonXpath, String usernameXpath, String passwordXpath, String verifycodeXpath,
	                         String verifyCodeUrl, String username, String password) {

		AnalogLoginEntity analogLogin = new AnalogLoginEntity();
		analogLogin.setTargetUrl(loginUrl);
		analogLogin.setHost(UrlUtils.getHost(loginUrl));
		analogLogin.setDomain(UrlUtils.getDomain(loginUrl));
		analogLogin.setLoginButtonXpath(loginButtonXpath);
		analogLogin.setUsernameXpath(usernameXpath);
		analogLogin.setPasswordXpath(passwordXpath);
		analogLogin.setVerifycodeXpath(verifycodeXpath);
		analogLogin.setVerifycodeUrl(verifyCodeUrl);
		analogLogin.setUsername(username);
		analogLogin.setPassword(password);

		return analogLoginService.saveAnalogLogin(analogLogin);

	}

	@RequestMapping("/loginAnalog")
	public Set<Cookie> loginAnalog(int id) {

		Set<Cookie> cookies = null;
		try {
			cookies = loginAnalog.login(id);
		} catch (Exception e) {
			logger.error("模拟登录失败！Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return cookies;
	}

	@RequestMapping("/jietu")
	public void jietu() {

		try {
			jieTu.savePage2Pic("http://115.233.227.46:18080/zqdata/login/verifyCode?random=0.7304289337922849", null, "vc-" + System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping("/test")
	public void xxxxxx(String jsid) throws InterruptedException {
		PhantomJSDriver driver = null;

		driver.get("http://114.55.11.227:8881/citysystemgz/loginIndex");
		driver.manage().deleteAllCookies();
		System.out.println(driver.manage().getCookies());
		Cookie cookie = new Cookie("JSESSIONID", jsid, "114.55.11.227", null, null);
		driver.manage().addCookie(cookie);
		WebElement element1 = driver.findElementByXPath("//*[@id=\"accountNameId\"]");
		element1.clear();
		element1.sendKeys("admin");
		System.out.println(element1.getAttribute("value"));
		WebElement element2 = driver.findElementByXPath("//*[@id=\"passwordId\"]");
		element2.clear();
		element2.sendKeys("1223");
		System.out.println(element2.getAttribute("value"));
		WebElement element3 = driver.findElementByXPath("//*[@id=\"verifyCodeId\"]");
		Map<String, Object> jtResult = null;
		try {
			jtResult = jieTu
					.savePage2Pic("http://114.55.11.227:8881/citysystemgz/verifyCode/slogin.do?random=0.8938614707267099", null,
					              "vc-" + System.currentTimeMillis() + "-" + new Random().nextInt(100));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String cjyBack = ChaoJiYing.PostPic("chenxiaoqi", "123456", "896782", "1902", "0", jtResult.get(SpiderConstant.IMAGE_PATH).toString());
		ChaoJiYingResult cjyResult = JSONUtil.toBean(cjyBack, ChaoJiYingResult.class);
		element3.clear();
		element3.sendKeys(cjyResult.getPic_str());
		System.out.println(element3.getAttribute("value"));
		WebElement element4 = driver.findElementByXPath("//*[@id=\"loginBtn\"]");
		System.out.println(element4.getText());
		element4.click();

		System.out.println(driver.getPageSource());
		Thread.sleep(3000);
		System.out.println(driver.getCurrentUrl());
		System.out.println(driver.manage().toString());

	}

	@RequestMapping("/getContent")
	public void getContent() {

		SpiderRule spiderRule = new SpiderRule();
		spiderPage.startSpider(1, "https://tool.lu/", false, false, spiderRule, null, new SpiderContentPipeline());

	}

}