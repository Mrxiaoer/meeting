package com.spider.modules.business.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 信息项表
 * <p> title: ElementInfoEntity </p> 
 * @author yaonuan
 * @data 2018年7月10日
 * version 1.0
 */
@TableName("spider_element_info")
public class ElementInfoEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    //编码
    @TableId
    private Integer elementId;
    //中文名
    private String nameCn;
    //英文名
    private String nameEn;
    //來源
    private String source;
    //资源项目名称
    private String informationName;
    //类型
    private String type;
    //类型字段长度
    private Integer len;
    //描述
    private String remark;
    //与link表的关联
    private Integer resultId;
    //默认值
    private String routine;
    //存在状态（1.存在;0.删除）
    @JsonIgnore
    private Integer state;
    
    public Integer getElementId() {
        return elementId;
    }
    public void setElementId(Integer elementId) {
        this.elementId = elementId;
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
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getInformationName() {
        return informationName;
    }
    public void setInformationName(String informationName) {
        this.informationName = informationName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Integer getLen() {
        return len;
    }
    public void setLen(Integer len) {
        this.len = len;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRoutine() {
        return routine;
    }
    public void setRoutine(String routine) {
        this.routine = routine;
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
    
}
