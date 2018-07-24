package com.spider.modules.spider.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 网站信息对象
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-21
 */
@Alias("analogLogin")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "spider_analog_login")
public class AnalogLoginEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	//编号
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	//登录链接
	@Column(columnDefinition = "varchar(127) default '' COMMENT '登录链接'", nullable = false)
	private String targetUrl;
	//host
	@Column(columnDefinition = "varchar(127) default '' COMMENT 'host'", nullable = false)
	private String host;
	//域名
	@Column(columnDefinition = "varchar(127) default '' COMMENT '域名'", nullable = false)
	private String domain;
	//cookie
	@Column(columnDefinition = "text COMMENT 'cookie'")
	private String cookie;
	//登录按钮xpath
	@Column(columnDefinition = "varchar(127) COMMENT '登录按钮xpath'")
	private String loginButtonXpath;
	//登录页用户名xpath
	@Column(columnDefinition = "varchar(127) COMMENT '登录页用户名xpath'")
	private String usernameXpath;
	//登录页密码xpath
	@Column(columnDefinition = "varchar(127) COMMENT '登录页密码xpath'")
	private String passwordXpath;
	//登录页验证码xpath
	@Column(columnDefinition = "varchar(127) COMMENT '登录页验证码xpath'")
	private String verifycodeXpath;
	//登录页用户名
	@Column(columnDefinition = "varchar(31) COMMENT '登录页用户名'")
	private String username;
	//登录页密码
	@Column(columnDefinition = "varchar(31) COMMENT '登录页密码'")
	private String password;
	//登录页验证码url
	@Column(columnDefinition = "varchar(255) COMMENT '登录页验证码url'")
	private String verifycodeUrl;
	//当前获取到的页面的url
	@Column(columnDefinition = "varchar(255) COMMENT '当前获取到的页面的url'")
	private String nowUrl;
	//内容页标记字符串
	@Column(columnDefinition = "varchar(255) COMMENT '内容页标记字符串'")
	private String strOfIndex;
	//是否启用[0否、1是]
	@Column(columnDefinition = "int(1) default 1 COMMENT '是否启用[0否、1是]'", nullable = false)
	private Integer isEnable;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getLoginButtonXpath() {
		return loginButtonXpath;
	}

	public void setLoginButtonXpath(String loginButtonXpath) {
		this.loginButtonXpath = loginButtonXpath;
	}

	public String getUsernameXpath() {
		return usernameXpath;
	}

	public void setUsernameXpath(String usernameXpath) {
		this.usernameXpath = usernameXpath;
	}

	public String getPasswordXpath() {
		return passwordXpath;
	}

	public void setPasswordXpath(String passwordXpath) {
		this.passwordXpath = passwordXpath;
	}

	public String getVerifycodeXpath() {
		return verifycodeXpath;
	}

	public void setVerifycodeXpath(String verifycodeXpath) {
		this.verifycodeXpath = verifycodeXpath;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerifycodeUrl() {
		return verifycodeUrl;
	}

	public void setVerifycodeUrl(String verifycodeUrl) {
		this.verifycodeUrl = verifycodeUrl;
	}

	public String getNowUrl() {
		return nowUrl;
	}

	public void setNowUrl(String nowUrl) {
		this.nowUrl = nowUrl;
	}

	public String getStrOfIndex() {
		return strOfIndex;
	}

	public void setStrOfIndex(String strOfIndex) {
		this.strOfIndex = strOfIndex;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}
}
