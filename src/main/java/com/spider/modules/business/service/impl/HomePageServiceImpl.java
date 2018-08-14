package com.spider.modules.business.service.impl;

import com.spider.modules.business.dao.HomePageChartDao;
import com.spider.modules.business.dao.LinkInfoDao;
import com.spider.modules.business.dao.PageInfoDao;
import com.spider.modules.business.dao.ResultInfoDao;
import com.spider.modules.business.model.HomePageChart;
import com.spider.modules.business.model.PageCountModel;
import com.spider.modules.business.service.HomePageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页接口类的实现
 *
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2018-08-13
 */
@Service
public class HomePageServiceImpl implements HomePageService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LinkInfoDao linkInfoDao;

    @Autowired
    ResultInfoDao resultInfoDao;

    @Autowired
    PageInfoDao pageInfoDao;

    @Autowired
    HomePageChartDao homePageChartDao;

    @Override
    public List<PageCountModel> getInfoCount(){
        Map<String,Object> map = linkInfoDao.countSystemAndModule();
        List<PageCountModel> count = new ArrayList<PageCountModel>();
        PageCountModel one = new PageCountModel();
        PageCountModel two = new PageCountModel();
        one.setId(1);
        one.setTitle("采集系统总数");
        one.setCount(Integer.valueOf(String.valueOf(map.get("countsystem"))));
        count.add(one);

        two.setId(2);
        two.setTitle("采集模块总数");
        two.setCount(Integer.valueOf(String.valueOf(map.get("countmodule"))));
        count.add(two);

        PageCountModel three = new PageCountModel();
        three.setId(3);
        three.setTitle("采集信息资源总数");
        three.setCount(resultInfoDao.pageCount());
        count.add(three);

        PageCountModel four = new PageCountModel();
        four.setId(4);
        four.setTitle("采集信息项总数");
        four.setCount(pageInfoDao.querySum(null));
        count.add(four);

        return count;
    }

    @Override
    public Map<String,Object> getTrendData(){
        Map<String,Object> map = new HashMap<String, Object>();
        List<String> xAxisData = new ArrayList<>();
        for (Integer  i= 1 ; i<13 ;i++){
            xAxisData.add(String.valueOf(i) + "月");
        }
        map.put("xAxisData", xAxisData);

        List<HomePageChart> seriesData = new  ArrayList<HomePageChart>();

        //统计系统与模块总数
//        List<Map<String,Object>> sandm = linkInfoDao.spiderByMonth();
//        HomePageChart oldHome = new HomePageChart();
//        HomePageChart home = new HomePageChart();
//        for (Map<String,Object> m:sandm){
//
//            oldHome.setName("采集模块总数");
//            oldHome.setMonth(Integer.valueOf(String.valueOf(m.get("month"))));
//            HomePageChart old = homePageChartDao.selectOne(oldHome);
//            oldHome.setSqldata(Integer.parseInt(String.valueOf(m.get("module"))));
//            this.insertOrUpdate(oldHome, old);
//
//            home.setName("采集系统总数");
//            home.setMonth(Integer.valueOf(String.valueOf(m.get("month"))));
//            HomePageChart other = homePageChartDao.selectOne(home);
//            home.setSqldata(Integer.parseInt(String.valueOf(m.get("system"))));
//            this.insertOrUpdate(home, other);
//        }

        seriesData.add(this.conversion("采集系统总数"));
        seriesData.add(this.conversion("采集模块总数"));
        seriesData.add(this.conversion("采集信息资源总数"));
        seriesData.add(this.conversion("采集信息项总数"));

        map.put("seriesData", seriesData);
        return map;
    }

    private  HomePageChart conversion(String name){

        HomePageChart homePageChart = new HomePageChart();
        homePageChart.setName(name);
        List<Integer> sqldata = homePageChartDao.selectAllByName(homePageChart);
        homePageChart.setType("bar");
        homePageChart.setData(sqldata);
        return homePageChart;
    }
}
