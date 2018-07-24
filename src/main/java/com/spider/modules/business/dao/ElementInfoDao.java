package com.spider.modules.business.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.spider.modules.business.entity.ElementInfoEntity;
/**
 * <p> title: ElementInfoDao </p> 
 * @author yaonuan
 * @data 2018年7月12日
 * version 1.0
 */
@Mapper
public interface ElementInfoDao extends BaseMapper<ElementInfoEntity> {
    /**
         * 根据id查询数据
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
          * 添加信息
     */
    Integer insert(ElementInfoEntity elementInfo);
    
    /**
          * 删除数据，逻辑删除
     * @param elementIds
     * @return
     */
    int delete(@Param("elementIds") Integer[] elementIds);
    
}
