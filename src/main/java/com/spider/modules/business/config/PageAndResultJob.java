package com.spider.modules.business.config;

import com.spider.common.utils.Constant;
import com.spider.modules.business.dao.PageInfoDao;
import com.spider.modules.business.dao.ResultInfoDao;
import com.spider.modules.business.entity.PageInfoEntity;
import com.spider.modules.business.entity.ResultInfoEntity;
import com.spider.modules.business.model.VcCodeImagePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 定时删除informationName空的数据
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2018-08-08
 */

@Component
public class PageAndResultJob {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PageInfoDao pageInfoDao;

    @Autowired
    VcCodeImagePath vcCodeImagePath;

    @Autowired
    ResultInfoDao resultInfoDao;

    @Scheduled(cron = "0 0 0/12 * * *")
    public void pageAndResult(){
        List<PageInfoEntity> pages = pageInfoDao.queryAll();
        PageInfoEntity pa = new PageInfoEntity();
        for (PageInfoEntity page : pages){
            if(page.getInformationName() == null){
                pa.setPageId(page.getPageId());
                pa.setState(Constant.VALUE_ZERO);
                pageInfoDao.update(pa);
            }
        }
        List<ResultInfoEntity> resultInfos = resultInfoDao.queryAll();
        ResultInfoEntity rs = new ResultInfoEntity();
        for (ResultInfoEntity resultInfo : resultInfos){
            if(pageInfoDao.listByResultId(resultInfo.getId()).size() == 0){
                rs.setId(resultInfo.getId());
                rs.setState(Constant.VALUE_ZERO);
                resultInfoDao.update(rs);
            }
        }
    }


    @Scheduled(cron = "0 0 0/12 * * *")
    public void deleteByPictures(){
        //定于时间删除2天前的文件夹
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        String data =dateFormat.format(calendar.getTime());

        String filename = vcCodeImagePath.getNewpath() + data;


    }


}
