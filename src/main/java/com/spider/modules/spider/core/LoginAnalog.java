package com.spider.modules.spider.core;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.spider.modules.spider.config.PhantomJSDriverPool;
import com.spider.modules.spider.config.SpiderConstant;
import com.spider.modules.spider.dao.AnalogLoginDao;
import com.spider.modules.spider.entity.AnalogLoginEntity;
import com.spider.modules.spider.entity.ChaoJiYingResult;
import com.spider.modules.spider.utils.*;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模拟登录 ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-28
 */
@Service
public class LoginAnalog {

	private final AnalogLoginDao analogLoginDao;
	private final JieTu jieTu;
	private final PhantomJSDriverPool phantomJSDriverPool;
	private Logger logger = LoggerFactory.getLogger(LoginAnalog.class);
	@Value("${chaojiying.username}")
	private String cjyUsername;
	@Value("${chaojiying.password}")
	private String cjyPassword;
	@Value("${chaojiying.softId}")
	private String cjySoftId;
	@Value("${image.p-path}")
	private String destDir;

	@Autowired
	public LoginAnalog(AnalogLoginDao analogLoginDao, JieTu jieTu, PhantomJSDriverPool phantomJSDriverPool) {
		this.analogLoginDao = analogLoginDao;
		this.jieTu = jieTu;
		this.phantomJSDriverPool = phantomJSDriverPool;
	}

	/**
	 * 模拟登录操作，在此之前请把用户名，密码存入数据库
	 *
	 * @param id
	 * @throws Exception
	 */
	public Set<Cookie> login(int id) throws Exception {

		Set<Cookie> resultCookies = null;

		AnalogLoginEntity sie = new AnalogLoginEntity();
		sie.setId(id);
		AnalogLoginEntity loginInfo = analogLoginDao.queryAnalogLoginLimit1(sie);

		String targetUrl = loginInfo.getTargetUrl();
		//断言targetUrl不为空
		Assert.notEmpty(targetUrl, "模拟登录--断言失败,targetUrl不为空!");

		//模拟浏览器创建连接，发起请求
		PhantomJSDriver driver = phantomJSDriverPool.borrowPhantomJSDriver();

        // 创建 Pattern 对象
        Pattern p = Pattern.compile("(https?://[\\S]*?)[^A-Z|a-z|0-9|\\u4e00-\\u9fa5|.|/|:|_|-]");
		try {
			//执行时间超出预算的话中断并抛出异常
			Class[] paramClzs = {String.class};
			Object[] paramObjs = {targetUrl};
			int timeOut = 10000;
			try {
				long startTime = System.currentTimeMillis();
				RunTimeout.timeoutMethod(driver, "get", paramClzs, paramObjs, timeOut);
				logger.info("点击事件耗时{}毫秒！", System.currentTimeMillis() - startTime);
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
            Matcher m = p.matcher(driver.getCurrentUrl());
            String beforeUrl;
            if (m.find()) {
                beforeUrl = m.group(1);
            }else {
                beforeUrl = driver.getCurrentUrl();
            }
			//url相同说明可直接获取目标页
			if (!beforeUrl.equals(targetUrl)) {
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1;
				int day = cal.get(Calendar.DATE);
				//尝试2次
				boolean flag = true;
				int tryNum = 0;
				int maxTry = 3;
				while (flag && tryNum < maxTry) {
					loginInfo = analogLoginDao.queryAnalogLoginLimit1(sie);
					String usernameXpath = loginInfo.getUsernameXpath();
					String usernameValue = loginInfo.getUsername();
					String passwordXpath = loginInfo.getPasswordXpath();
					String passwordValue = loginInfo.getPassword();
					String verifyCodeXpath = loginInfo.getVerifycodeXpath();
					String loginButtonXpath = loginInfo.getLoginButtonXpath();

					String verifycodeUrl = loginInfo.getVerifycodeUrl();
					String verifyCodeValue = null;
					ChaoJiYingResult cjyResult = null;
					if (verifycodeUrl != null) {
						//截取验证码
						Map<String, Object> jtResult =
								jieTu.savePage2Pic(verifycodeUrl, cookieSet, "vc-" + System.currentTimeMillis() + "-" + new Random().nextInt(100));
						cookieSet = (Set<Cookie>) jtResult.get(SpiderConstant.COOKIES);
						int cjyTry = 0;
						boolean cjyFlag = true;

						if (tryNum < 1) {
							//ocr解析验证码
							File file = new File(jtResult.get(SpiderConstant.IMAGE_PATH).toString());
							PicUril.cleanLinesInImage(file, destDir + year + "-" + month + "-" + day);
							verifyCodeValue = OCRUtil.identifyCode(file);
							cjyFlag = false;
						} else {
							while (cjyTry < 3 && cjyFlag) {
								//超级鹰解析验证码
								String cjyBack = ChaoJiYing.PostPic(cjyUsername, cjyPassword, cjySoftId, "1902", "0",
								                                    jtResult.get(SpiderConstant.IMAGE_PATH).toString());
								cjyResult = JSONUtil.toBean(cjyBack, ChaoJiYingResult.class);
								cjyTry++;
								if ("-3001".equals(cjyResult.getErr_no()) || "-3002".equals(cjyResult.getErr_no())) {
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
						if (StrUtil.isNotBlank(verifyCodeXpath)) {
							WebElement verifyCodeElement = driver.findElementByXPath(verifyCodeXpath);
							verifyCodeElement.clear();
							verifyCodeElement.sendKeys(verifyCodeValue);
							logger.info("用户名：{} ；密码：{} ；验证码：{} ；", usernameElement.getAttribute("value"), passwordElement.getAttribute("value"),
							            verifyCodeElement.getAttribute("value"));
							logger.info("==>模拟登录--有验证码，进行尝试，URL=" + targetUrl);
						} else {
							logger.info("用户名：{} ；密码：{} ；", usernameElement.getAttribute("value"), passwordElement.getAttribute("value"));
							logger.info("==>模拟登录--无验证码，进行尝试，URL=" + targetUrl);
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
                        Matcher mnew = p.matcher(driver.getCurrentUrl());
                        String nowUrl;
                        if (m.find()) {
                            nowUrl = mnew.group(1);
                        }else {
                            nowUrl = driver.getCurrentUrl();
                        }
						while (sleepNum < 5 && URLUtil.getPath(nowUrl).equals(URLUtil.getPath(beforeUrl))) {
							Thread.sleep(1000);
							sleepNum++;
						}
						logger.info("当前页URL：{}，原页面：{}", nowUrl,beforeUrl);
					} catch (Exception e) {
						throw e;
					} finally {
						tryNum++;
						//检测登录，判断是否已登录，已登录flag为false
						if (!URLUtil.getPath(driver.getCurrentUrl()).equals(URLUtil.getPath(beforeUrl))) {
							flag = false;
						} else if (cjyResult != null) {
							//向超级鹰报错
							ChaoJiYing.ReportError(cjyUsername, cjyPassword, cjySoftId, cjyResult.getPic_id());
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
				resultCookies = driver.manage().getCookies();
			}
		} finally {
			phantomJSDriverPool.returnObject(driver);
		}
		return resultCookies;
	}

}