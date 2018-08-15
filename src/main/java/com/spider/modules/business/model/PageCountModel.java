package com.spider.modules.business.model;


import java.io.Serializable;

/**
 * 统计系统，模块，信息项，采集条数
 *
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2018-08-13
 */
public class PageCountModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String title;

    private Integer count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
