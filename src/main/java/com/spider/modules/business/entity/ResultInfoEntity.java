package com.spider.modules.business.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

@TableName("spider_result_info")
public class ResultInfoEntity  implements Serializable{

    private static final long serialVersionUID = 1L;
    
    //编码
    @TableId
    private Integer id;
    
    //系统名称
    private String system;
    
    //模块名称
    private String module;
    
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",locale="zh",timezone="GMT+8")
    private Date createTime;
    
    //是否是模板
    private Integer isModel;
    
    //链接信息表的主键
    private Integer linkId;

    //是否转化<0、未转化 1、转化未完全 2、转化完全>
    private Integer changeState;
    //存在状态（1.存在;0.删除）
    private Integer state;
    
    //引入pageInfoEntity
    @Transient
    private List<PageInfoEntity> pageInfo;
    
    @Transient
    private List<ElementInfoEntity> elementInfo;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsModel() {
        return isModel;
    }

    public void setIsModel(Integer isModel) {
        this.isModel = isModel;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public Integer getChangeState() {
        return changeState;
    }

    public void setChangeState(Integer changeState) {
        this.changeState = changeState;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<PageInfoEntity> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(List<PageInfoEntity> pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<ElementInfoEntity> getElementInfo() {
        return elementInfo;
    }

    public void setElementInfo(List<ElementInfoEntity> elementInfo) {
        this.elementInfo = elementInfo;
    }
}
