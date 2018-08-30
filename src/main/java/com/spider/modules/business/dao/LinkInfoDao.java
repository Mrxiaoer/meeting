package com.spider.modules.business.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.spider.modules.business.entity.LinkInfoEntity;
/**
 * <p> title: linkInfoDao </p> 
 * @author yaonuan
 * @data 2018年7月9日
 * version 1.0
 */
@Mapper
public interface LinkInfoDao extends BaseMapper<LinkInfoEntity>{
    
    /**
     * 查询根据id
     * @param linkId
     * @return
     */
    LinkInfoEntity queryById(Integer linkId);

    /**
     * 自动化采集显示专用
     * @param linkId
     * @return
     */
    LinkInfoEntity selectById(Integer linkId);

    /**
     * 根据ids删除链接表信息
     * @param linkIds
     * @return
     */
    Integer deletebyLinkId(@Param("linkIds")Integer[] linkIds);
    
    /**
     * 增加链接信息
     * @param linkInfo
     * @return
     */
    Integer insert(LinkInfoEntity linkInfo);
    
    /**
     *  根据自定义条件更新链接表信息
     * @param linkInfo
     * @return
     */
    int update(LinkInfoEntity linkInfo);

    /**
     * 自定义精准查询
     * @param linkInfo
     * @return
     */
    List<LinkInfoEntity> queryTerm(LinkInfoEntity linkInfo);

    /**
     * 自动采集 tree-structured
     * @return
     */
    List<LinkInfoEntity> selectByHasTarget();

    /**
     * 首页统计系统名称与模块名称
     * @return
     */
    Map<String,Object> countSystemAndModule();

    /**
     * 采集首页中系统与模块总数
     * @return
     */
    List<Map<String,Object>> spiderByMonth();
}
