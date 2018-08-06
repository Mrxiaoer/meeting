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
     * 查询所有数据
     * @param linkInfo
     * @return
     */
    List<LinkInfoEntity> queryList();
    
    /**
     * 查询根据id
     * @param linkId
     * @return
     */
    LinkInfoEntity queryById(Integer linkId);

    // TODO: 2018/8/6 0006  需解决scheduleJobServiceImpl
    List<LinkInfoEntity> queryByIds(Integer linkId);
    
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
     * 定时任务联动一级
     * @return
     */
    List<String> querySum();

    /**
     * 自定义精准查询
     * @param linkInfo
     * @return
     */
    List<LinkInfoEntity> queryTerm(LinkInfoEntity linkInfo);

    List<LinkInfoEntity> selectAll();
}
