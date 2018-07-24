package com.spider.modules.business.service;
/**
 * 
 * <p> title: LinkInfoService </p> 
 * @author yaonuan
 * @data 2018年7月9日
 * version 1.0
 */

import java.util.Map;

import com.spider.common.utils.PageUtils;
import com.spider.modules.business.entity.LinkInfoEntity;
/**
 * 链接信息查詢
 */
public interface LinkInfoService {
    
    
    /**
     * 根据id查询数据
     * @param linkId
     * @return
     */
    LinkInfoEntity queryById(Integer linkId);
    
    
    /**
     * 根据自定义条件分页查询
     * @param linkInfo
     * @return
     */
    PageUtils queryTerm(Map<String, Object> params);
    
    /**
     * 添加链接信息
     * @param params
     */
    void save(Map<String,Object> params);
    
    /**
     * 更新链接信息
     * @param linkInfo
     */
    void update(LinkInfoEntity linkInfo);
    
    /**
     * 根据id查询删除信息
     * @param LinkIds
     */
    void deletebyLinkId(Integer[] linkIds);
    
}
