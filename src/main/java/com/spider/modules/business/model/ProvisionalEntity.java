package com.spider.modules.business.model;

import java.io.Serializable;

public class ProvisionalEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer pageId;

    private String nameCn;
    
    private String nameEn;
    
    private String source;
    
    private String informationName;
    
    private Integer resultId;
    
    private String type;
    
    private Integer len;
    
    private String remark;
    
    private String routine;

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

    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(Integer resultId) {
        this.resultId = resultId;
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
    
    
    
}
