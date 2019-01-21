package com.meeting.modules.business.service;

import java.util.List;
import java.util.Map;

import com.meeting.common.utils.PageUtils;
import com.meeting.modules.business.entity.PageInfoEntity;
import com.meeting.modules.business.entity.ResultInfoEntity;

public interface ResultInfoService {

    /**
     * 结果页信息查询
     * @param resultId
     * @return
     */
    List<ResultInfoEntity> queryByResultId(Integer resultId);

    /**
     * 插入数据
     * @param resultInfo
     */
    void save(ResultInfoEntity resultInfo);

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
     * 转换查询
     * @param params
     * @return
     */
    PageUtils queryChallenge(Map<String,Object> params);
    
    /**
     * 比对信息
     * @param id
     * @return
     */
    List<PageInfoEntity> comparison(Integer id);

    /**
     * 数据转换
     * @param map
     * @return
     */
    List<ResultInfoEntity> resultByPageInfo(Map<String,Object> map);

    /**
     * 采集结果=>查看=>详情
     * param id
     */
    List<ResultInfoEntity> conversionByInformation(Integer linkId);

    /**
     * 查模板信息
     * @return
     */
    PageUtils queryModel(Map<String, Object> params);

    /**
     * 设置模板或取消模板
     * @param id
     */
    void setTemplate(Integer id);
}
