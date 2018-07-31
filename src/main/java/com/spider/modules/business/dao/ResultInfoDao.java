package com.spider.modules.business.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.spider.modules.business.entity.ResultInfoEntity;
import com.spider.modules.business.model.ResultInfoModel;

@Mapper
public interface ResultInfoDao extends BaseMapper<ResultInfoModel> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    ResultInfoEntity queryById(Integer id);
    
    /**
     * 多表查采集结果中element信息
     * @param resultId
     * @return
     */
    List<ResultInfoEntity> queryByResultId(Integer resultId);
    
    /**
     * 多表查采集结果中pageInfo信息
     * @param resultInfo
     * @return list
     */
    List<ResultInfoEntity> listByResultId(ResultInfoEntity resultInfo);

    /**
     * 采集结果=>查看=>详情
     * @param linkId
     * @return
     */
    List<ResultInfoEntity> conversionByInformation(@Param("linkId") Integer linkId);
    
    /**
     * 更新数据
     * @param resultInfo
     * @return
     */
    int update(ResultInfoEntity resultInfo);
    
    /**
     * 资源目录
     * @return
     */
    List<Map<String, Object>> querySum();
    
    /**
     * 添加数据
     */
    Integer insert(ResultInfoEntity resultInfo);
    
    /**
     * 删除数据，逻辑删除
     * @param ids
     * @return
     */
    int delete(@Param("ids") Set<Integer> ids);
}
