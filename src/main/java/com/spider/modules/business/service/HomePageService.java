package com.spider.modules.business.service;


import com.spider.modules.business.model.PageCountModel;

import java.util.List;
import java.util.Map;

public interface HomePageService {

    /**
     * 首页统计总数信息
     * @return
     */
    List<PageCountModel> getInfoCount();

    /**
     * 图表信息显示
     * @return
     */
    Map<String,Object> getTrendData();
}
