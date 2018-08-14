package com.spider.modules.business.dao;

import com.spider.modules.business.model.HomePageChart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HomePageChartDao {

    /**
     * 查询月份（12个月份）信息
     * @return
     */
    List<Integer> queryMonth();

    /**
     * 查询sqldata数据信息
     * @param homePageChart
     * @return
     */
    List<Integer> selectAllByName(HomePageChart homePageChart);

    /**
     * 插入一条
     * @param homePageChart
     * @return
     */
    int insertOne(HomePageChart homePageChart);

    /**
     * 更新一条
     * @param homePageChart
     * @return
     */
    int updateOne(HomePageChart homePageChart);

    /**
     * 查找一条
     * @param homePageChart
     * @return
     */
    HomePageChart selectOne(HomePageChart homePageChart);
}
