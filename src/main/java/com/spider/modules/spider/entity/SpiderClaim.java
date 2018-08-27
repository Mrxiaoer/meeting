package com.spider.modules.spider.entity;

import java.util.Set;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 爬取要求
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-08-27
 */
public class SpiderClaim {

	private boolean allDomain = false;
	private boolean isStaticPage = false;
	private Set<Cookie> cookieSet;
	private Pipeline pipeline;
	private PhantomJSDriver phantomJSDriver;
	private int sleepTime = 1500;

	public boolean isAllDomain() {
		return allDomain;
	}

	public void setAllDomain(boolean allDomain) {
		this.allDomain = allDomain;
	}

	public boolean isStaticPage() {
		return isStaticPage;
	}

	public void setStaticPage(boolean staticPage) {
		isStaticPage = staticPage;
	}

	public Set<Cookie> getCookieSet() {
		return cookieSet;
	}

	public void setCookieSet(Set<Cookie> cookieSet) {
		this.cookieSet = cookieSet;
	}

	public Pipeline getPipeline() {
		return pipeline;
	}

	public void setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
	}

	public PhantomJSDriver getPhantomJSDriver() {
		return phantomJSDriver;
	}

	public void setPhantomJSDriver(PhantomJSDriver phantomJSDriver) {
		this.phantomJSDriver = phantomJSDriver;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
}
