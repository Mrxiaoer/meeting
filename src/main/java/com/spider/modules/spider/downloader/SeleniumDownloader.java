package com.spider.modules.spider.downloader;

import com.spider.modules.spider.config.PhantomJSDriverPool;
import com.spider.modules.spider.entity.MyPage;
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
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

import java.io.Closeable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 动态页面下载器
 * ------------------------------
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
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private int sleepTime = 100;

	public SeleniumDownloader(String chromeDriverPath) {
		System.getProperties().setProperty("webdriver.chrome.driver", chromeDriverPath);
	}

	public SeleniumDownloader() {}

	public SeleniumDownloader setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
		return this;
	}

	@Override
	public MyPage download(Request request, Task task) {

		PhantomJSDriver driver = null;
		try {
			driver = phantomJSDriverPool.borrowObject();
		} catch (InterruptedException var10) {
			this.logger.warn("interrupted", var10);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		assert (driver != null);

		try {
			Thread.sleep((long) this.sleepTime);
		} catch (InterruptedException var9) {
			var9.printStackTrace();
		}

		WebDriver.Options manage = driver.manage();

		Site site = task.getSite();
		if (site.getCookies() != null) {

			for (Object o : site.getCookies().entrySet()) {
				Map.Entry<String, String> cookieEntry = (Map.Entry) o;
				Cookie cookie = new Cookie(cookieEntry.getKey(), cookieEntry.getValue());
				manage.addCookie(cookie);
			}
		}
		if (site.getAllCookies() != null) {
			Set<Map.Entry<String, Map<String, String>>> allCookieSet = site.getAllCookies().entrySet();

			for (Map.Entry<String, Map<String, String>> cookiemap : allCookieSet) {
				for (Map.Entry<String, String> cookieEntry : cookiemap.getValue().entrySet()) {
					try {
						manage.addCookie(new Cookie(cookieEntry.getKey(), cookieEntry.getValue(), cookiemap.getKey(), "/", null));
					} catch (Exception e) {
						manage.addCookie(new Cookie(cookieEntry.getKey(), cookieEntry.getValue(), "." + cookiemap.getKey(), "/", null));
					}
				}
			}
		}

		this.logger.info("downloading page " + request.getUrl());
		driver.get(request.getUrl());

		WebElement webElement = driver.findElement(By.xpath("/html"));
		String content = webElement.getAttribute("outerHTML");
		MyPage page = new MyPage();
		page.setRawText(content);
		page.setHtml(new Html(content, request.getUrl()));
		page.setUrl(new PlainText(driver.getCurrentUrl()));
		//设置path
		String urlPath = null;
		if (driver.manage().getCookies() != null) {
			Iterator<Cookie> cookies = driver.manage().getCookies().iterator();
			if (cookies.hasNext()) {
				Cookie cookieEntry = cookies.next();
				urlPath = cookieEntry.getPath();
			}
		}
		page.setUrlPath(urlPath);
		page.setRequest(request);
		this.phantomJSDriverPool.returnObject(driver);
		return page;
	}

	@Override
	public void setThread(int threadNum) {

	}

	@Override
	public void close() {
		logger.info("close this downloader!");
	}
}

