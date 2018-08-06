package com.spider.modules.business.model;

import com.spider.modules.business.entity.LinkInfoEntity;

import java.util.List;

/**
 * @Author: yaonuan
 * @Date: 2018/8/3 0003 下午 10:36
 */
public class TimeTaskModel {

    private String label;
    private String value;

    private List<TimeTaskModel> children;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TimeTaskModel> getChildren() {
        return children;
    }

    public void setChildren(List<TimeTaskModel> children) {
        this.children = children;
    }
}
