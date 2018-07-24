package com.spider.modules.business.service;

import java.util.Map;

import com.spider.common.utils.PageUtils;
import com.spider.modules.business.entity.ElementInfoEntity;

public interface ElementInfoService {
    
    
    /**
     * 根据element_id查询数据
     * @param elementId
     * @return
     */
    ElementInfoEntity queryById(Integer elementId);
    
    /**
     * 更新数据
     * @param elementInfo
     * @return
     */
    int update(ElementInfoEntity elementInfo);
    
    /**
     * 根据自定义(nameCn)条件查询
     * @param params
     * @return
     */
    PageUtils queryTerm(Map<String, Object> params);
    
    /**
     * 提交转化信息
     * @param elementInfo
     * @return
     */
    void save(ElementInfoEntity elementInfo);
    
    /**
     * 数据逻辑删除，根据element_id
     * @param elementIds
     * @return
     */
    void delete(Integer[] elementIds);
}
