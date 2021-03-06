package com.meeting.modules.business.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.meeting.modules.business.entity.PageInfoEntity;
/**
 * <p> title: ElementInfoDao </p> 
 * @author yaonuan
 * @data 2018年7月12日
 * version 1.0
 */
@Mapper
public interface PageInfoDao extends BaseMapper<PageInfoEntity> {

    /**
     * 查询数据库中pageinfo表所有数据
     * @return
     */
   List<PageInfoEntity> queryAll();
    /**
     * 根据id查询数据
     * @param pageId
     * @return
     */
    PageInfoEntity queryById(Integer pageId);
    
    /**
     * 根据resultId查询未转化的信息
     * @param resultId
     * @return
     */
    List<PageInfoEntity> queryByResultId(Integer resultId);

    /**
     * 根据resultId查询信息
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
     * 更新数据
     * @param pageInfo
     * @return
     */
    int update(PageInfoEntity pageInfo);
    
    /**
     * 添加数据
     */
    Integer insert(PageInfoEntity pageInfo);
    
    /**
     * 删除数据，逻辑删除
     * @param pageIds
     * @return
     */
    int delete(@Param("pageIds") Integer[] pageIds);

    /**
     * 统计pageInfo中关于resultId的信息
     * @param map
     * @return
     */
    int querySum(Map<String,Object> map);

   /**
    * 定时统计采集信息项
    * @return
    */
   List<Map<String,Object>> spiderByMonth();
}
