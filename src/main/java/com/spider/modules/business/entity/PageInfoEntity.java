package com.spider.modules.business.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@TableName("spide_page_info")
public class PageInfoEntity implements Serializable{

    private static final long serialVersionUID = 1L;
    
    //编码
    @TableId
    private Integer pageId;
    
    //字段中文名
    private String nameCn;
    
    //字段英文名
    private String nameEn;
    
    //资源目录名
    private String informationName;
    
    
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;
    
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;
    
    //是否转化
    private Integer isChange;
    
    //结果表的主键
    private Integer resultId;
    
    //存在状态(1、存在  2、删除)
    @JsonIgnore
    private Integer state;

    @Transient
    private LinkInfoEntity link;
    
    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getInformationName() {
        return informationName;
    }

    public void setInformationName(String informationName) {
        this.informationName = informationName;
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

    public Integer getIsChange() {
        return isChange;
    }

    public void setIsChange(Integer isChange) {
        this.isChange = isChange;
    }

    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(Integer resultId) {
        this.resultId = resultId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public LinkInfoEntity getLink() {
        return link;
    }

    public void setLink(LinkInfoEntity link) {
        this.link = link;
    }
    
}
