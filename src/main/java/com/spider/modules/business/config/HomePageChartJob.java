package com.spider.modules.business.config;

import com.spider.modules.business.dao.HomePageChartDao;
import com.spider.modules.business.dao.LinkInfoDao;
import com.spider.modules.business.dao.PageInfoDao;
import com.spider.modules.business.dao.ResultInfoDao;
import com.spider.modules.business.model.HomePageChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 首页图表数据的定时任务
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2018-08-14
 */
@Component
public class HomePageChartJob {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LinkInfoDao linkInfoDao;

    @Autowired
    HomePageChartDao homePageChartDao;

    @Autowired
    ResultInfoDao resultInfoDao;

    @Autowired
    PageInfoDao pageInfoDao;

    @Scheduled(cron = "0 0 0/12 * * *")
    public void TimingCollection(){

        //定时统计系统总数与模块总数
        List<Map<String,Object>> map = linkInfoDao.spiderByMonth();
        for (Map<String,Object> m:map){
            HomePageChart oldHome = new HomePageChart();
            HomePageChart home = new HomePageChart();

            oldHome.setName("采集模块总数");
            oldHome.setMonth(Integer.valueOf(String.valueOf(m.get("month"))));
            HomePageChart old = homePageChartDao.selectOne(oldHome);
            oldHome.setSqldata(Integer.parseInt(String.valueOf(m.get("module"))));
            this.insertOrUpdate(oldHome, old);

            home.setName("采集系统总数");
            home.setMonth(Integer.valueOf(String.valueOf(m.get("month"))));
            HomePageChart other = homePageChartDao.selectOne(home);
            home.setSqldata(Integer.parseInt(String.valueOf(m.get("system"))));
            this.insertOrUpdate(home, other);
        }
        //定时统计信息项总数
        List<Map<String,Object>> re = resultInfoDao.spiderByMonth();
        for (Map<String,Object> q:re){
            HomePageChart information = new HomePageChart();
            information.setName("采集信息资源总数");
            information.setMonth(Integer.valueOf(String.valueOf(q.get("month"))));
            HomePageChart resource = homePageChartDao.selectOne(information);
            information.setSqldata(Integer.parseInt(String.valueOf(q.get("result"))));
            this.insertOrUpdate(information, resource);
        }
        //定时统计采集信息项总数
        List<Map<String,Object>> pa = pageInfoDao.spiderByMonth();
        for (Map<String,Object> p :pa){
            HomePageChart item = new HomePageChart();
            item.setName("采集信息项总数");
            item.setMonth(Integer.valueOf(String.valueOf(p.get("month"))));
            HomePageChart it = homePageChartDao.selectOne(item);
            item.setSqldata(Integer.parseInt(String.valueOf(p.get("page"))));
            this.insertOrUpdate(item, it);
        }
    }

    @Scheduled(cron = "0 0/3 * * * *")
    public void addByMonth(){
        List<String> system = new ArrayList<>();
        system.add("采集系统总数");
        system.add("采集模块总数");
        system.add("采集信息资源总数");
        system.add("采集信息项总数");
        for(String s:system){
            for(Integer i=1;i<13;i++){
                HomePageChart one = new HomePageChart();
                one.setMonth(i);
                one.setName(s);
                HomePageChart two = homePageChartDao.selectOne(one);
                this.insertOrUpdate(one, two);
            }
        }
    }

    private void insertOrUpdate(HomePageChart now,HomePageChart old){
        if(old == null){
            now.setType("bar");
            homePageChartDao.insertOne(now);
        }else{
            now.setId(old.getId());
            homePageChartDao.updateOne(now);
        }
    }
}
