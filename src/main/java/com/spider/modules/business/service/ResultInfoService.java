package com.spider.modules.business.service;

import java.util.List;
import java.util.Map;

import com.spider.common.utils.PageUtils;
import com.spider.modules.business.entity.ResultInfoEntity;

public interface ResultInfoService {

    /**
     * 插入数据
     * @param resultInfo
     */
    void save(ResultInfoEntity resultInfo);
    
    /**
     * 结果页信息查询
     * @param resultId
     * @return
     */
    List<ResultInfoEntity> queryByResultId(Integer resultId);
    
    
    /**
     * 资源目录汇总
     * @return
     */
    List<Map<String, Object>> querySum();
    
    /**
     * 根据条件自定义查询分页
     * @param params
     * @return
     */
    PageUtils queryTerm(Map<String,Object> params);
    
    /**
     * 查模板信息
     * @return
     */
    PageUtils queryModel(Map<String, Object> params);
    
    /**
     * 比对
     */
    Map<String,Object> comparison(Integer currentId,Integer modelId);
}
