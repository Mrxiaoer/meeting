package com.spider.modules.business.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Transient;
import java.util.List;

/**
 * 图表数据模型
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2018-08-13
 */
@TableName("spider_home_page_chart")
public class HomePageChart {

    /**
     * 主键id
     */
    @TableId
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 数据库中存储数据
     */
    @JsonIgnore
    private Integer sqldata;

    /**
     * 前端显示的数据
     */
    @Transient
    private List<Integer> data;

    /**
     * 月份
     */
    private Integer month;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSqldata() {
        return sqldata;
    }

    public void setSqldata(Integer sqldata) {
        this.sqldata = sqldata;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
}
