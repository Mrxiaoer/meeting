package com.spider.modules.business.service.impl;

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

        List<Integer> systemcount = new ArrayList<Integer>();
        List<Integer> modulecount = new ArrayList<Integer>();
        List<Integer> resultcount = new ArrayList<Integer>();
        List<Integer> pagecount = new ArrayList<Integer>();
        //初始list数据
        for(Integer i=0;i<12;i++){
            systemcount.add(0);
            modulecount.add(0);
            resultcount.add(0);
            pagecount.add(0);
        }

        //统计系统与模块总数
        List<Map<String,Object>> sandm = linkInfoDao.spiderByMonth();
        for (Map<String,Object> m:sandm){
            Integer s = Integer.parseInt(String.valueOf(m.get("month"))) - 1;
            systemcount.set(s, Integer.parseInt(String.valueOf(m.get("system"))));
            modulecount.set(s, Integer.parseInt(String.valueOf(m.get("module"))));
        }
        //定时统计信息项总数
        List<Map<String,Object>> re = resultInfoDao.spiderByMonth();
        for (Map<String,Object> q:re){
            Integer m = Integer.parseInt(String.valueOf(q.get("month"))) - 1;
            resultcount.set(m, Integer.parseInt(String.valueOf(q.get("result"))));
        }
        //定时统计采集信息项总数
        List<Map<String,Object>> pa = pageInfoDao.spiderByMonth();
        for (Map<String,Object> p :pa){
            Integer y = Integer.parseInt(String.valueOf(p.get("month"))) - 1;
            pagecount.set(y, Integer.parseInt(String.valueOf(p.get("page"))));
        }
        //组装entity
        HomePageChart system = new HomePageChart();
        system.setName("采集系统总数");
        system.setType("bar");
        system.setData(systemcount);
        seriesData.add(system);

        HomePageChart module = new HomePageChart();
        module.setName("采集模块总数");
        module.setType("bar");
        module.setData(modulecount);
        seriesData.add(module);

        HomePageChart result = new HomePageChart();
        result.setName("采集信息资源总数");
        result.setType("bar");
        result.setData(resultcount);
        seriesData.add(result);

        HomePageChart page = new HomePageChart();
        page.setName("采集信息项总数");
        page.setType("bar");
        page.setData(pagecount);
        seriesData.add(page);

        map.put("seriesData", seriesData);
        return map;
    }

}
