package com.spider.modules.spider.entity;

import us.codecraft.webmagic.Page;

/**
 * 我的page，继承自webmagic
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-14
 */
public class MyPage extends Page {

	private String urlPath;

	public static MyPage fail() {
		MyPage page = new MyPage();
		page.setDownloadSuccess(false);
		return page;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	@Override
	public MyPage setSkip(boolean skip) {
		super.getResultItems().setSkip(skip);
		return this;

	}

	@Override
	public MyPage setRawText(String rawText) {
		super.setRawText(rawText);
		return this;
	}

	@Override
	public String toString() {
		return "MyPage{" + "urlPath='" + urlPath + '\'' + "} " + super.toString();
	}
}
