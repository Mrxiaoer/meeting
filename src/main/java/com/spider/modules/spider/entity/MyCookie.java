package com.spider.modules.spider.entity;

import java.util.Date;

/**
 * cookie，作中转用
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-24
 */
public class MyCookie {

	private String name;
	private String value;
	private String path;
	private String domain;
	private Date expiry;
	private boolean isSecure;
	private boolean isHttpOnly;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public boolean isSecure() {
		return isSecure;
	}

	public void setSecure(boolean secure) {
		isSecure = secure;
	}

	public boolean isHttpOnly() {
		return isHttpOnly;
	}

	public void setHttpOnly(boolean httpOnly) {
		isHttpOnly = httpOnly;
	}
}
