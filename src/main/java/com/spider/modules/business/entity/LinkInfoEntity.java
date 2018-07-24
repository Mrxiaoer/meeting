package com.spider.modules.business.entity;

import java.io.Serializable;
import java.util.Date;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 链接信息表
 * <p>
 * title: LinkInfo
 * </p>
 * 
 * @author yaonuan
 * @data 2018年7月9日 version 1.0
 */
@TableName("spider_link_info")
public class LinkInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // 编号
    @TableId
    private Integer linkId;
    
    //模拟登录表主键
    private Integer analogId;
    
    // 所属站点系统
    private String system;

    // 所属系统模块
    private String module;

    // 此系统待采集模块url
    private String url;
    
    private Integer isLogin;

    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    // 修改时间
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    // 描述
    private String remarks;
    
    //目标页是否可达（1可达，0不可达）
    private Integer hasTarget;
    
    //关联规则表Id
    @JsonIgnore
    private Integer ruleId;

    // 存在状态（1.存在;0.删除）
    @JsonIgnore
    private Integer state;

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public Integer getAnalogId() {
        return analogId;
    }

    public void setAnalogId(Integer analogId) {
        this.analogId = analogId;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Integer isLogin) {
        this.isLogin = isLogin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getHasTarget() {
        return hasTarget;
    }

    public void setHasTarget(Integer hasTarget) {
        this.hasTarget = hasTarget;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

}
