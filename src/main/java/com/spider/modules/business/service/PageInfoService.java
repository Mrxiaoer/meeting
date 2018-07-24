package com.spider.modules.business.service;

import java.util.List;
import java.util.Map;

import com.spider.modules.business.entity.PageInfoEntity;
import com.spider.modules.business.model.ProvisionalEntity;

public interface PageInfoService {

    /**
     * 插入数据
     * @param pageInfo
     */
    void save(PageInfoEntity pageInfo);
    
    /**
     * 信息转化
     * @param params
     */
    void updatePageAndElement(ProvisionalEntity provisional);
    
    /**
     * 根据结果表id进行查询
     * @param resultId
     * @return
     */
    List<PageInfoEntity> queryByResultId(Integer resultId);
    
    /**
     * 采集结果=>查看=>详情
     * @param pageId
     * @return
     */
    PageInfoEntity queryByPageId(Integer pageId);
    
    /**
     * 根据信息更新内容
     * @param pageInfo
     */
    void update(PageInfoEntity pageInfo);
    
    
}
