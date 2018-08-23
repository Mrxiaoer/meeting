package com.spider.modules.spider.core;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.spider.modules.spider.config.PhantomJSDriverFactory;
import com.spider.modules.spider.config.SpiderConstant;
import com.spider.modules.spider.dao.AnalogLoginDao;
import com.spider.modules.spider.entity.AnalogLoginEntity;
import com.spider.modules.spider.entity.ChaoJiYingResult;
import com.spider.modules.spider.utils.ChaoJiYing;
import com.spider.modules.spider.utils.JieTu;
import com.spider.modules.spider.utils.MyStringUtil;
import com.spider.modules.spider.utils.OCRUtil;
import com.spider.modules.spider.utils.PicUril;
import com.spider.modules.spider.utils.RunTimeout;
import java.io.File;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 模拟登录
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-28
 */
@Service
public class LoginAnalog {

	private final AnalogLoginDao analogLoginDao;
	private final JieTu jieTu;
	private final PhantomJSDriverFactory phantomJSDriverFactory;
	private Logger logger = LoggerFactory.getLogger(LoginAnalog.class);
	@Value("${VcCode.chaojiying.username}")
	private String cjyUsername;
	@Value("${VcCode.chaojiying.password}")
	private String cjyPassword;
	@Value("${VcCode.chaojiying.softId}")
	private String cjySoftId;
	@Value("${VcCode.image.p-path}")
	private String destDir;
	@Value("${VcCode.useOcr}")
	private boolean useOcr;

	@Autowired
	public LoginAnalog(AnalogLoginDao analogLoginDao, JieTu jieTu, PhantomJSDriverFactory phantomJSDriverFactory) {
		this.analogLoginDao = analogLoginDao;
		this.jieTu = jieTu;
		this.phantomJSDriverFactory = phantomJSDriverFactory;
	}

	/**
	 * 模拟登录操作，在此之前请把用户名，密码存入数据库
	 */
	public Set<Cookie> login(int id, PhantomJSDriver driver) throws Exception {

		Set<Cookie> resultCookies;

		AnalogLoginEntity sie = new AnalogLoginEntity();
		sie.setId(id);
		AnalogLoginEntity loginInfo = analogLoginDao.queryAnalogLoginLimit1(sie);

		String targetUrl = loginInfo.getTargetUrl();
		String loginUrl = loginInfo.getLoginUrl();
		//断言targetUrl不为空
		Assert.notEmpty(targetUrl, "模拟登录--失败,targetUrl不能为空!");

		//执行时间超出预算的话中断并抛出异常
		Class[] paramClzs = {String.class};
		Object[] paramObjs = {targetUrl};
		int timeOut = 5000;
		boolean needChange = false;
		PhantomJSDriver oldDriver = null;
		try {
			try {
				long startTime = System.currentTimeMillis();
				RunTimeout.timeoutMethod(driver, "get", paramClzs, paramObjs, timeOut);
				logger.info("获取页面耗时{}毫秒！", System.currentTimeMillis() - startTime);

				//直接获取到目标页，则说明此浏览器驱动保留有此页面相关信息，则重新启动一个驱动用于访问
				if (MyStringUtil.urlCutParam(driver.getCurrentUrl()).equals(targetUrl)) {
					//替换驱动，在本次处理结束后退出驱动，并将原驱动返回池
					needChange = true;
					oldDriver = driver;
					driver = phantomJSDriverFactory.create();
					RunTimeout.timeoutMethod(driver, "get", paramClzs, paramObjs, timeOut);
				}
			} catch (RuntimeException re) {
				//让程序继续往下执行
				logger.info("获取页面超过{}毫秒！停止等待，向下执行！", timeOut);
				//			throw new RuntimeException("获取页面超时！操作取消！");
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}

			//获取cookies
			Set<Cookie> cookieSet = driver.manage().getCookies();
			String beforeUrl = MyStringUtil.urlCutParam(driver.getCurrentUrl());
			//url相同说明可直接获取目标页
			if (!beforeUrl.equals(targetUrl)) {
				//当前页非预期登录页则获取预期登录页
				if (StrUtil.isNotBlank(loginUrl) && !beforeUrl.equals(MyStringUtil.urlCutParam(loginUrl))) {
					driver.get(loginUrl);
				}
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1;
				int day = cal.get(Calendar.DATE);
				//尝试n次
				boolean flag = true;
				int tryNum = 0;
				int maxTry = 3;
				while (flag && tryNum < maxTry) {
					//					loginInfo = analogLoginDao.queryAnalogLoginLimit1(sie);
					String usernameXpath = loginInfo.getUsernameXpath();
					String usernameValue = loginInfo.getUsername();
					String passwordXpath = loginInfo.getPasswordXpath();
					String passwordValue = loginInfo.getPassword();
					String verifyCodeXpath = loginInfo.getVerifycodeXpath();
					//					String verifycodePicXpath = loginInfo.getVerifycodeUrl();
					String loginButtonXpath = loginInfo.getLoginButtonXpath();

					String verifyCodeUrl = loginInfo.getVerifycodeUrl();
					String verifyCodeValue = null;
					ChaoJiYingResult cjyResult = null;
					if (StrUtil.isNotBlank(verifyCodeXpath) && StrUtil.isNotBlank(verifyCodeUrl)) {
						//截取验证码
						Map<String, Object> jtResult = jieTu.savePage2Pic(verifyCodeUrl, cookieSet,
								"vc-" + System.currentTimeMillis() + "-" + new Random().nextInt(100));
						//						jieTu.saveVc(driver,verifycodePicXpath,"vc-" + System.currentTimeMillis() +
						// "-" + new Random().nextInt(100));
						cookieSet = (Set<Cookie>) jtResult.get(SpiderConstant.COOKIES);
						int cjyTry = 0;
						boolean cjyFlag = true;

						if (useOcr && tryNum < 1) {
							//ocr解析验证码
							File file = new File(jtResult.get(SpiderConstant.IMAGE_PATH).toString());
							File returnFile = PicUril.cleanLinesInImage(file, destDir + year + "-" + month + "-" + day);
							verifyCodeValue = OCRUtil.identifyCode(returnFile);
							cjyFlag = false;
						} else {
							while (cjyTry < 3 && cjyFlag) {
								//超级鹰解析验证码
								String cjyBack = ChaoJiYing.PostPic(cjyUsername, cjyPassword, cjySoftId, "1902", "0",
										jtResult.get(SpiderConstant.IMAGE_PATH).toString());
								cjyResult = JSONUtil.toBean(cjyBack, ChaoJiYingResult.class);
								cjyTry++;
								if ("-3001" .equals(cjyResult.getErr_no()) || "-3002" .equals(cjyResult.getErr_no())) {
									logger.info("==>超级鹰--请求超时！重试");
									continue;
								}
								if (StrUtil.isBlank(cjyResult.getPic_str())) {
									continue;
								}
								verifyCodeValue = cjyResult.getPic_str();
								cjyFlag = false;
							}
						}
						if (cjyFlag) {
							logger.info("==>解析验证码--失败次数过多");
						}
					}

					//模拟登录获取cookies
					try {
						WebElement usernameElement = driver.findElementByXPath(usernameXpath);
						usernameElement.clear();
						usernameElement.sendKeys(usernameValue);
						WebElement passwordElement = driver.findElementByXPath(passwordXpath);
						passwordElement.clear();
						passwordElement.sendKeys(passwordValue);
						//是否有验证码
						if (StrUtil.isNotBlank(verifyCodeXpath) && StrUtil.isNotBlank(verifyCodeValue)) {
							WebElement verifyCodeElement;
							//							try {
							verifyCodeElement = driver.findElementByXPath(verifyCodeXpath);
							verifyCodeElement.clear();
							verifyCodeElement.sendKeys(verifyCodeValue);
							logger.info("用户名：{} ；密码：{} ；验证码：{} ；", usernameElement.getAttribute("value"),
									passwordElement.getAttribute("value"), verifyCodeElement.getAttribute("value"));
							logger.info("==>模拟登录--有验证码，进行尝试，URL={}", targetUrl);
							//							} catch (NoSuchElementException nse) {
							//								logger.error("没有此验证码元素{}", nse);
							//								logger.info("用户名：{} ；密码：{} ；", usernameElement.getAttribute
							// ("value"),
							//										passwordElement.getAttribute("value"));
							//								logger.info("==>模拟登录--无验证码，进行尝试，URL={}", targetUrl);
							//							}
						} else {
							logger.info("用户名：{} ；密码：{} ；", usernameElement.getAttribute("value"),
									passwordElement.getAttribute("value"));
							logger.info("==>模拟登录--无验证码，进行尝试，URL={}", targetUrl);
						}
						//模拟点击
						WebElement loginButtonElement = driver.findElementByXPath(loginButtonXpath);
						//执行时间超出预算的话中断并抛出异常
						Class[] paramClzs1 = {};
						Object[] paramObjs1 = {};
						int outTime = 8000;
						try {
							long startTime = System.currentTimeMillis();
							RunTimeout.timeoutMethod(loginButtonElement, "click", paramClzs1, paramObjs1, outTime);
							logger.info("点击事件耗时{}毫秒！", System.currentTimeMillis() - startTime);
						} catch (RuntimeException re) {
							logger.info("点击事件耗时超过{}毫秒！停止等待，向下执行！", outTime);
						} catch (Exception e) {
							e.printStackTrace();
							throw e;
						}

						//sleep,等待
						int sleepNum = 0;
						String nowUrl = MyStringUtil.urlCutParam(driver.getCurrentUrl());
						while (sleepNum < 5 && URLUtil.getPath(nowUrl).equals(URLUtil.getPath(beforeUrl))) {
							Thread.sleep(1000);
							sleepNum++;
						}
						logger.info("当前页URL：{}，原页面：{}", nowUrl, beforeUrl);
					} finally {
						tryNum++;
						//检测登录，判断是否已登录，已登录flag为false
						String tarUrl = MyStringUtil.urlCutParam(targetUrl);
						driver.get(targetUrl);
						String nowUrl = MyStringUtil.urlCutParam(driver.getCurrentUrl());
						if (nowUrl.equals(tarUrl)) {
							flag = false;
						} else {
							if (cjyResult != null) {
								//向超级鹰报错
								ChaoJiYing.ReportError(cjyUsername, cjyPassword, cjySoftId, cjyResult.getPic_id());
							}
							logger.info("登录失败！" + (tryNum < maxTry ? "重试。。。" : ""));
						}
					}
				}

				//判断登录是否成功
				if (flag) {
					logger.info("==>模拟登录--失败，URL=" + targetUrl);
					resultCookies = null;
				} else {
					logger.info("==>模拟登录--成功，URL=" + targetUrl);
					resultCookies = driver.manage().getCookies();
					loginInfo.setCookie(MyStringUtil.cookie2json(resultCookies));
					analogLoginDao.updateAnalogLogin(loginInfo);
				}
			} else {
				resultCookies = driver.manage().getCookies();
				loginInfo.setCookie(MyStringUtil.cookie2json(resultCookies));
				analogLoginDao.updateAnalogLogin(loginInfo);
				resultCookies = driver.manage().getCookies();
			}
		} finally {
			if (needChange) {
				driver.quit();
				driver = oldDriver;
				assert (driver != null);
			}
		}
		return resultCookies;
	}

}