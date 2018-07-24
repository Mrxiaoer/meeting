package com.spider.modules.business.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.xalan.xsltc.compiler.sym;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import com.spider.common.utils.Constant;
import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.business.entity.PageInfoEntity;
import com.spider.modules.business.entity.ResultInfoEntity;
import com.spider.modules.business.model.TargetInfoEntity;
import com.spider.modules.business.service.LinkInfoService;
import com.spider.modules.business.service.PageInfoService;
import com.spider.modules.business.service.ResultInfoService;
import com.spider.modules.business.service.TargetInfoService;
import com.spider.modules.spider.core.HtmlProcess;
import com.spider.modules.spider.core.LoginAnalog;
import com.spider.modules.spider.core.SpiderPage;
import com.spider.modules.spider.dao.AnalogLoginDao;
import com.spider.modules.spider.dao.TemporaryRecordDao;
import com.spider.modules.spider.entity.AnalogLoginEntity;
import com.spider.modules.spider.entity.SpiderRule;
import com.spider.modules.spider.entity.TemporaryRecordEntity;
import com.spider.modules.spider.pipeline.SpiderTemporaryRecordPipeline;
import com.spider.modules.spider.service.AnalogLoginService;
import com.spider.modules.spider.service.TemporaryRecordService;
import com.spider.modules.spider.utils.MyStringUtil;

import cn.hutool.core.bean.BeanUtil;

@Service
public class TargetInfoServiceImpl implements TargetInfoService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    AnalogLoginDao analogLoginDao;
    
    @Autowired
    TemporaryRecordDao temporaryRecordDao;

    @Autowired
    private LoginAnalog loginAnalog;
    
    @Autowired
    AnalogLoginService analogLoginService;
    
    @Autowired
    LinkInfoService linkInfoService;
    
    @Autowired
    TemporaryRecordService temporaryRecordService;
    
    @Autowired
    ResultInfoService resultInfoService;
    
    @Autowired
    PageInfoService pageInfoService;
    
    @Autowired
    private SpiderPage spiderPage;
    
    @Autowired
    SpiderTemporaryRecordPipeline spiderTemporaryRecordPipeline;
    
    @Autowired
    HtmlProcess htmlprocess;

    /**
     * 模拟登录返回登录页
     */
    @Override
    public String tospider(Integer linkId) {
        
        LinkInfoEntity linkInfo = linkInfoService.queryById(linkId);
        //logger.info("linkinfo"+linkInfo);
        SpiderRule spiderRule = new SpiderRule();
        spiderRule.setIsGetText(false);
        spiderPage.startSpider(linkId, linkInfo.getUrl(), false, false, spiderRule, null, 
                spiderTemporaryRecordPipeline);
        TemporaryRecordEntity rc = new TemporaryRecordEntity();
        rc = temporaryRecordService.queryBylinkId(linkId);
        
        return rc.getHtml();
    }
    /*模拟登录第2步*/
    @Override
    public LinkInfoEntity update(TargetInfoEntity targetInfo, Integer analogId) {
        AnalogLoginEntity analogLogin = new AnalogLoginEntity();
        BeanUtil.copyProperties(targetInfo, analogLogin);
        analogLogin.setId(analogId);
        analogLoginDao.updateAnalogLogin(analogLogin);
        java.util.Set<Cookie> cookies = null;
        try {
            cookies =  loginAnalog.login(analogLogin.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(cookies != null) {
            LinkInfoEntity linkInfo = new LinkInfoEntity();
            linkInfo.setHasTarget(Constant.SUPER_ADMIN);
            linkInfo.setLinkId(targetInfo.getLinkId());
            linkInfoService.update(linkInfo);
        }
        LinkInfoEntity link = linkInfoService.queryById(targetInfo.getLinkId());
        //模拟登录后采集目标页
        SpiderRule spiderRule = new SpiderRule();
        spiderRule.setIsGetText(false);
        spiderPage.startSpider(targetInfo.getLinkId(), targetInfo.getUrl(), false, false, spiderRule, cookies, spiderTemporaryRecordPipeline);
        
        return link;
    }
    
    /*
     * cookie的问题需要解决
     */
    @Override
    public TemporaryRecordEntity tothirdspider(Integer linkId) {
        
        LinkInfoEntity linkInfo = linkInfoService.queryById(linkId);
        AnalogLoginEntity analogLogin = analogLoginService.getOneById(linkInfo.getAnalogId());
        //解决cookie String转map
        String cookie = analogLogin.getCookie();
        System.err.println(cookie.length());
        Integer j = 1;
        for(int i=1;i<cookie.length()-1;i++) {
            if(cookie.substring(i, i+1) != ";") {
              System.err.println(cookie.substring(i,i+1));;
            }else {
                String key = cookie.substring(j, i+1);
                j=i+1;
                System.err.println(key);
            }
            
        }
        System.err.println(analogLogin.getCookie());
        
        SpiderRule spiderRule = new SpiderRule();
        spiderRule.setIsGetText(false);
        
     /*   Set<Cookie> cookies = analogLogin.getCookie();
        spiderPage.startSpider(analogLogin.getTargetUrl(), false, false, spiderRule, analogLogin.getCookie(), 
                spiderTemporaryRecordPipeline);*/
        
        TemporaryRecordEntity rc = new TemporaryRecordEntity();
        rc.setLinkId(linkId);
        rc.setUrl(linkInfo.getUrl());
        
        return  temporaryRecordDao.selectOne(rc);
        
    }
    /**
     * 页面爬取获得list<String>
     * 同时插入resultinfo与pageinfo表中
     * 并返回给前端处理类
     */
    @Override
    public Map<String,Object> getXpath(Map<String, Object> params) {
        String xpath = (String) params.get("xpath");
        Integer linkId = (Integer) params.get("linkId");
        LinkInfoEntity linkInfo = linkInfoService.queryById(linkId);
        String url = linkInfo.getUrl();
        SpiderRule spiderRule = new SpiderRule();
        spiderRule.setXpath(xpath);
        List<String> spiderHead = htmlprocess.process(url, spiderRule);
        ResultInfoEntity resultInfo = new ResultInfoEntity();
        resultInfo.setSystem(linkInfo.getSystem());
        resultInfo.setModule(linkInfo.getModule());
        resultInfo.setCreatTime(new Date());
        resultInfo.setLinkId(linkInfo.getLinkId());
        resultInfoService.save(resultInfo);
        /**
         * 采集到的表头插入数据库中
         */
        for(String vaule:spiderHead) {
           PageInfoEntity pageInfo = new PageInfoEntity();
           pageInfo.setNameCn(vaule);
           pageInfo.setResultId(resultInfo.getId());
           MyStringUtil myStringUtil = new MyStringUtil();
           pageInfo.setNameEn(myStringUtil.getPinYinHeadChar(vaule));
           pageInfoService.save(pageInfo);
        }
        /**
         * 采集的结果返回给前端
         */
        List<PageInfoEntity> pageInfos = pageInfoService.queryByResultId(resultInfo.getId());
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pageInfos", pageInfos);
        return map;
        
    }
    @Override
    public void updateHead(Map<String, Object> params) {
        String AllinformationName = (String) params.get("informationName");
        List<Map<String, Object>> map =  (List<Map<String, Object>>) params.get("pageInfos");
        for(Map<String, Object> list : map) {
            PageInfoEntity pageInfo = new PageInfoEntity();
            pageInfo.setInformationName(AllinformationName);
            pageInfo.setPageId((Integer) list.get("pageId"));
            pageInfo.setState((Integer) list.get("state"));
            pageInfoService.update(pageInfo);
        }
    }

}
