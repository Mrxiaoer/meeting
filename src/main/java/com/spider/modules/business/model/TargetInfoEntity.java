package com.spider.modules.business.model;

import java.io.Serializable;

public class TargetInfoEntity implements Serializable{

    private static final long serialVersionUID = 1L;
    //链接表的编码
    private Integer linkId;
    //编号
    private Integer id;
    //登录链接
    private String url;
    //host
    private String host;
    //域名
    private String domain;
    //cookie
    private String cookie;
    //登录按钮xpath
    private String loginButtonXpath;
    //登录页用户名xpath
    private String usernameXpath;
    //登录页密码xpath
    private String passwordXpath;
    //登录页验证码xpath
    private String verifycodeXpath;
    //登录页用户名
    private String username;
    //登录页密码
    private String password;
    //登录页验证码url
    private String verifycodeUrl;
    //登录页标记字符串
    private String strOfLogin;
    //内容页标记字符串
    private String strOfIndex;
    //是否启用[0否、1是]
    private Integer isEnable;
    //登录页验证码图片xpath
    private String verifycodePicXpath;
    
    public Integer getLinkId() {
        return linkId;
    }
    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
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
    public String getStrOfLogin() {
        return strOfLogin;
    }
    public void setStrOfLogin(String strOfLogin) {
        this.strOfLogin = strOfLogin;
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

    public String getVerifycodePicXpath() {
        return verifycodePicXpath;
    }

    public void setVerifycodePicXpath(String verifycodePicXpath) {
        this.verifycodePicXpath = verifycodePicXpath;
    }
}
