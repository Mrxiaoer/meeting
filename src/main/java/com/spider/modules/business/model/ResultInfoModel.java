package com.spider.modules.business.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

@TableName("spider_result_info")
public class ResultInfoModel  implements Serializable{

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
    private Date creatTime;
    
    //是否是模板
    private Integer isModel;
    
    //链接信息表的主键
    private Integer linkId;
    
    //存在状态（1.存在;0.删除）
    private Integer state;

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

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

}

