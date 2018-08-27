package com.spider.modules.spider.downloader;

import com.spider.modules.spider.config.PhantomJSDriverFactory;
import com.spider.modules.spider.config.PhantomJSDriverPool;
import com.spider.modules.spider.entity.MyPage;
import com.spider.modules.spider.entity.MySite;
import com.spider.modules.spider.utils.MyStringUtil;
import com.spider.modules.spider.utils.RunTimeout;
import java.io.Closeable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

/**
 * 动态页面下载器
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-26
 */
@Component
public class SeleniumDownloader implements Downloader, Closeable {

	private static final String DRIVER_PHANTOMJS = "phantomjs";
	@Autowired
	private PhantomJSDriverPool phantomJSDriverPool;
	@Autowired
	private PhantomJSDriverFactory phantomJSDriverFactory;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private int sleepTime = 2000;

	public SeleniumDownloader(String chromeDriverPath) {
		System.getProperties().setProperty("webdriver.chrome.driver", chromeDriverPath);
	}

	public SeleniumDownloader() {
	}

	public SeleniumDownloader setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
		return this;
	}

	@Override
	public MyPage download(Request request, Task task) {
		MySite site = (MySite) task.getSite();

		PhantomJSDriver driver = site.getPhantomJSDriver();
		MyPage page = new MyPage();
		boolean needReturn = false;
		if (driver == null) {
			needReturn = true;
			try {
				driver = phantomJSDriverPool.borrowPhantomJSDriver();
			} catch (InterruptedException var10) {
				this.logger.warn("interrupted", var10);
				return null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		boolean needChange = false;
		PhantomJSDriver oldDriver = null;
		try {
			assert (driver != null);

			//			driver.get(request.getUrl());
			WebDriver.Options manage = driver.manage();
			//			manage.deleteAllCookies();

			logger.info("开始设置cookie");
			long setCookieStartTime = System.currentTimeMillis();
			if (site.getCookies() != null) {
				for (Object o : site.getCookies().entrySet()) {
					Map.Entry<String, String> cookieEntry = (Map.Entry) o;
					Cookie cookie = new Cookie(cookieEntry.getKey(), cookieEntry.getValue());
					manage.addCookie(cookie);
				}
			}
			if (site.getAllCookies() != null) {
				Set<Map.Entry<String, Map<String, String>>> allCookieSet = site.getAllCookies().entrySet();

				boolean canSet;
				canSet = setCookies(manage, allCookieSet);
				if (!canSet) {
					driver.get(request.getUrl());
					manage.deleteAllCookies();
					canSet = setCookies(manage, allCookieSet);
				}
			}
			logger.info("设置cookie用时{}毫秒", System.currentTimeMillis() - setCookieStartTime);

			logger.info("开始下载页面 -- {}", request.getUrl());
			long downloadStartTime = System.currentTimeMillis();
			//			driver.get(request.getUrl());
			Class[] paramClzs = {String.class};
			Object[] paramObjs = {request.getUrl()};
			int timeOut = 9000;
			try {
				RunTimeout.timeoutMethod(driver, "get", paramClzs, paramObjs, timeOut);
			} catch (ExecutionException | InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("当前页 -- {}", driver.getCurrentUrl());
			if (!MyStringUtil.urlCutParam(request.getUrl()).equals(MyStringUtil.urlCutParam(driver.getCurrentUrl()))) {
				needChange = true;
				oldDriver = driver;
				driver = phantomJSDriverFactory.create();
			}
			try {
				Thread.sleep((long) this.sleepTime);
				//				Thread.sleep(task.getSite().getSleepTime());
			} catch (InterruptedException var9) {
				var9.printStackTrace();
			}

			//获取页面与期待页面url对比，处理
			if (MyStringUtil.urlCutParam(driver.getCurrentUrl()).equals(MyStringUtil.urlCutParam(request.getUrl()))) {
				WebElement webElement = driver.findElement(By.xpath("/html"));
				String content = webElement.getAttribute("outerHTML");
				page.setRawText(content);
				page.setHtml(new Html(content, request.getUrl()));
				page.setUrl(new PlainText(driver.getCurrentUrl()));
				// 设置path
				String urlPath = null;
				if (driver.manage().getCookies() != null) {
					Iterator<Cookie> cookies = driver.manage().getCookies().iterator();
					if (cookies.hasNext()) {
						Cookie cookieEntry = cookies.next();
						urlPath = cookieEntry.getPath();
					}
				}
				page.setUrlPath(urlPath);
				logger.info("下载页面耗时{}毫秒", System.currentTimeMillis() - downloadStartTime);
			} else {
				logger.info("未获取到指定页面，耗时{}毫秒", System.currentTimeMillis() - downloadStartTime);
			}
			page.setRequest(request);

		} finally {
			if(needChange){
				driver.quit();
				driver = oldDriver;
			}

			if (needReturn) {
				try {
					this.phantomJSDriverPool.returnObject(driver);
				} catch (Exception e) {
					try {
						this.phantomJSDriverPool.invalidateObject(driver);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return page;
	}

	@Override
	public void setThread(int threadNum) {
	}

	@Override
	public void close() {
		logger.info("close this downloader!");
	}

	private boolean setCookies(WebDriver.Options manage, Set<Map.Entry<String, Map<String, String>>> allCookieSet) {
		boolean canSet = true;
		for (Map.Entry<String, Map<String, String>> cookiemap : allCookieSet) {
			for (Map.Entry<String, String> cookieEntry : cookiemap.getValue().entrySet()) {
				try {
					manage.addCookie(
							new Cookie(cookieEntry.getKey(), cookieEntry.getValue(), cookiemap.getKey(), "/", null));
				} catch (Exception e) {
					try {
						if (cookiemap.getKey().startsWith(".")) {
							manage.addCookie(new Cookie(cookieEntry.getKey(), cookieEntry.getValue(),
									cookiemap.getKey().substring(1, cookiemap.getKey().length() - 1), "/", null));
						} else {
							manage.addCookie(
									new Cookie(cookieEntry.getKey(), cookieEntry.getValue(), "." + cookiemap.getKey(), "/",
											null));
						}
					} catch (Exception ex) {
						canSet = false;
						return canSet;
					}
				}
			}
		}
		return canSet;
	}
}
