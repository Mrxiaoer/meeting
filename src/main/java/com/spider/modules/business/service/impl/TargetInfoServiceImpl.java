package com.spider.modules.business.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.spider.modules.business.dao.LinkInfoDao;
import com.spider.modules.spider.core.LoginAnalog;
import com.spider.modules.spider.dao.SpiderRuleDao;
import com.spider.modules.spider.service.SpiderRuleService;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

import javax.transaction.Transactional;

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
    SpiderRuleDao spiderRuleDao;
    
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

        return link;
    }
    
    @Override
    public TemporaryRecordEntity tothirdspider(Integer linkId) {
        LinkInfoEntity linkInfo = linkInfoService.queryById(linkId);
        AnalogLoginEntity analogLogin = analogLoginService.getOneById(linkInfo.getAnalogId());
        String cookie = analogLogin.getCookie();
//        System.err.println(cookie.length());
        Set<Cookie> cookies = MyStringUtil.json2cookie(cookie);
        SpiderRule spiderRule = new SpiderRule();
        spiderRule.setIsGetText(false);
        spiderPage.startSpider(linkId, analogLogin.getTargetUrl(), false, false, spiderRule, cookies, spiderTemporaryRecordPipeline);
        if(temporaryRecordService.queryBylinkId(linkId).getUrl() != analogLogin.getTargetUrl()){
            try {
                cookies =  loginAnalog.login(analogLogin.getId());
            }catch (Exception e){
                e.printStackTrace();
            }
            spiderPage.startSpider(linkId, analogLogin.getTargetUrl(), false, false, spiderRule, cookies, spiderTemporaryRecordPipeline);
        }
        return  temporaryRecordService.queryBylinkId(linkId);
        
    }

    @Override
//    @Transactional
    public Map<String,Object> getXpath(Map<String, Object> params) {
        String xpath = (String) params.get("xpath");
        //需要解决//*[@id="spiderContent"] 并非/html/body的问题转化 前端解决

        Integer linkId = (Integer) params.get("linkId");
        LinkInfoEntity linkInfo = linkInfoService.queryById(linkId);

        //xpath插入规则表
        SpiderRule rule = new SpiderRule();
        rule.setXpath(xpath);
        if( rule.getId() != null ){
            rule.setId(null);
        }
        spiderRuleDao.insertOne(rule);

        //更新linkInfo加入规则id
        LinkInfoEntity linkrule = new LinkInfoEntity();
        linkrule.setLinkId(linkId);
        linkrule.setRuleId(rule.getId());
        linkInfoService.update(linkrule);

        //组装解析表头信息
        SpiderRule spiderRule = new SpiderRule();
        spiderRule.setXpath(xpath);
        TemporaryRecordEntity te = new TemporaryRecordEntity();
        te.setLinkId(linkId);
        te.setUrl(linkInfo.getUrl());
        List<String> spiderHead = htmlprocess.process(te, spiderRule);
        ResultInfoEntity resultInfo = new ResultInfoEntity();
        resultInfo.setSystem(linkInfo.getSystem());
        resultInfo.setModule(linkInfo.getModule());
        resultInfo.setCreateTime(new Date());
        resultInfo.setLinkId(linkInfo.getLinkId());
        resultInfoService.save(resultInfo);
        
         //采集到的表头插入数据库中
        for(String vaule:spiderHead) {
           PageInfoEntity pageInfo = new PageInfoEntity();
           pageInfo.setNameCn(vaule);
           pageInfo.setResultId(resultInfo.getId());
           MyStringUtil myStringUtil = new MyStringUtil();
           pageInfo.setNameEn(myStringUtil.getPinYinHeadChar(vaule));
           pageInfoService.save(pageInfo);
        }
         //采集的结果返回给前端
        List<PageInfoEntity> pageInfos = pageInfoService.queryByResultId(resultInfo.getId());
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pageInfos", pageInfos);
        return map;
    }
    
    @Override
    public void updateHead(Map<String, Object> params) {
        String AllinformationName = (String) params.get("informationName");
        List<Map<String, Object>> map =  (List<Map<String, Object>>) params.get("pageInfos");
        Integer id = 0;
        for(Map<String, Object> list : map) {
            PageInfoEntity pageInfo = new PageInfoEntity();
            pageInfo.setInformationName(AllinformationName);
            pageInfo.setPageId((Integer) list.get("pageId"));
            pageInfo.setState((Integer) list.get("state"));
            pageInfoService.update(pageInfo);
            id = pageInfo.getPageId();
        }
        System.out.println(id);
        List<PageInfoEntity> pageInfos = pageInfoService.listByResultId(pageInfoService.queryById(id).getResultId());
        PageInfoEntity prs = new PageInfoEntity();
        for(PageInfoEntity list:pageInfos){
            if(list.getInformationName() == null){
                prs.setPageId(list.getPageId());
                prs.setState(Constant.VALUE_ZERO);
                pageInfoService.update(prs);
            }
        }
    }

}
