package com.meeting.modules.business.service;

import com.meeting.common.utils.PageUtils;
import com.meeting.modules.business.entity.PageInfoEntity;
import com.meeting.modules.business.model.ProvisionalEntity;

import java.util.List;
import java.util.Map;

public interface PageInfoService {

    /**
     * 根据pageId查询信息
     * @param pageId
     * @return
     */
    PageInfoEntity queryById(Integer pageId);

    /**
     * 根据结果表id进行查询未转化的信息
     * @param resultId
     * @return
     */
    List<PageInfoEntity> queryByResultId(Integer resultId);

    /**
     * 根据resultId查询信息（未转化+已转化）
     * @param resultId
     * @return
     */
    List<PageInfoEntity> listByResultId(Integer resultId);

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

    /**
     * 根据自定义条件查询
     * @param params
     * @return
     */
    PageUtils queryTerm(Map<String,Object> params);

    /**
     * 插入数据
     * @param pageInfo
     */
    void save(PageInfoEntity pageInfo);

    /**
     * 信息转化
     * @param provisional
     */
    void updatePageAndElement(ProvisionalEntity provisional);

    void delete(Integer[] pageIds);
    
}
