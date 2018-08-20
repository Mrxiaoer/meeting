package com.spider.modules.business.service;
/**
 * 
 * <p> title: LinkInfoService </p> 
 * @author yaonuan
 * @data 2018年7月9日
 * version 1.0
 */

import java.util.List;
import java.util.Map;

import com.spider.common.utils.PageUtils;
import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.business.model.TimeTaskModel;

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
     * @param params
     * @return
     */
    PageUtils queryTerm(Map<String, Object> params);

    /**
     * 精准查询
     * @param params
     * @return
     */
    PageUtils queryAccurate(Map<String,Object> params);
    
    /**
     * 添加链接信息
     * @param linkInfo
     */
    void save(LinkInfoEntity linkInfo);
    
    /**
     * 更新链接信息
     * @param linkInfo
     */
    void update(LinkInfoEntity linkInfo);
    
    /**
     * 根据id查询删除信息
     * @param linkIds
     */
    void deletebyLinkId(Integer[] linkIds);


    List<TimeTaskModel> timedTask();

    /**
     * 目标页登录不了，需重新登录
     * @param linkId
     */
    void updateByTarget(Integer linkId);
}
