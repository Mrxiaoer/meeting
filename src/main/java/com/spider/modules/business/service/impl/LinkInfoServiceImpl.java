package com.spider.modules.business.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.spider.common.utils.Constant;
import com.spider.common.utils.PageUtils;
import com.spider.common.utils.Query;
import com.spider.modules.business.dao.LinkInfoDao;
import com.spider.modules.business.dao.ResultInfoDao;
import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.business.entity.ResultInfoEntity;
import com.spider.modules.business.model.TimeTaskModel;
import com.spider.modules.business.service.LinkInfoService;
import com.spider.modules.spider.dao.AnalogLoginDao;
import com.spider.modules.spider.entity.AnalogLoginEntity;
import com.spider.modules.spider.service.AnalogLoginService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * <p> title: LinkInfoServiceImpl </p>
 * @author yaonuan
 * @data 2018年7月9日
 * version 1.0
 */
@Service
public class LinkInfoServiceImpl extends ServiceImpl<LinkInfoDao, LinkInfoEntity>  implements LinkInfoService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LinkInfoDao linkInfoDao;

    @Autowired
    AnalogLoginService analogLoginService;

    @Autowired
    AnalogLoginDao analogLoginDao;

    @Autowired
    ResultInfoDao resultInfoDao;

    @Override
    public PageUtils queryTerm(Map<String, Object> params) {
        String system = (String) params.get("system");
        String module = (String) params.get("module");
        Page<LinkInfoEntity> page = this.selectPage(new Query<LinkInfoEntity>(params).getPage(),
                new EntityWrapper<LinkInfoEntity>().like(StringUtils.isNotBlank(system),"system" ,system)
                .and().like(StringUtils.isNotBlank(module),"module" ,module).eq("state", Constant.TRUE_STATE)
                        .orderBy("link_id",false));
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryAccurate(Map<String,Object> params){
        String system = (String) params.get("system");
        Page<LinkInfoEntity> page = this.selectPage(new Query<LinkInfoEntity>(params).getPage(),
                new EntityWrapper<LinkInfoEntity>()
                        .eq("state", Constant.TRUE_STATE)
                        .eq("system",system)
                        );
        return  new PageUtils(page);
    }

    @Override
    public LinkInfoEntity queryById(Integer linkId) {
        return linkInfoDao.queryById(linkId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(LinkInfoEntity linkInfo) {
        AnalogLoginEntity analogLogin = new AnalogLoginEntity();

        //在AnalogLoginEntity表中存用户爬取的url
        analogLogin.setTargetUrl(linkInfo.getUrl());
        analogLogin.setLoginUrl(linkInfo.getLoginUrl());
        if(analogLogin.getId() !=null) {
            analogLogin.setId(null);
        }
        analogLoginService.saveAnalogLogin(analogLogin);

        linkInfo.setAnalogId(analogLogin.getId());
        linkInfo.setCreateTime(new Date());
        linkInfo.setUpdateTime(new Date());
        if(linkInfo.getLinkId() != null) {
            linkInfo.setLinkId(null);
        }
        linkInfoDao.insert(linkInfo);
    }


    @Override
    public void update(LinkInfoEntity linkInfo) {
        linkInfo.setUpdateTime(new Date());
        linkInfo.setHasTarget(Constant.VALUE_ZERO);
        linkInfoDao.update(linkInfo);

        if(linkInfo.getUrl() != null || linkInfo.getLoginUrl() != null) {
           AnalogLoginEntity rc = new AnalogLoginEntity();
           rc.setId(this.queryById(linkInfo.getLinkId()).getAnalogId());
           rc.setTargetUrl(linkInfo.getUrl());
           rc.setLoginUrl(linkInfo.getLoginUrl());
           analogLoginDao.updateAnalogLogin(rc);
        }
        //用户修改系统名称以及模块名称去更新采集结果表中的系统与模块名
        if(!(linkInfo.getSystem() == null || linkInfo.getModule() == null)){
            LinkInfoEntity oldlink = this.queryById(linkInfo.getLinkId());
            if(!oldlink.getSystem().equals(linkInfo.getSystem()) || !oldlink.getModule().equals(linkInfo.getModule())){
                ResultInfoEntity rs = new ResultInfoEntity();
                rs.setLinkId(linkInfo.getLinkId());
                ResultInfoEntity rsg = new ResultInfoEntity();
                rsg.setSystem(linkInfo.getSystem());
                rsg.setModule(linkInfo.getModule());
                List<ResultInfoEntity> results = resultInfoDao.selectByCustom(rs);
                for (ResultInfoEntity resultInfo:results){
                    rsg.setId(resultInfo.getId());
                    resultInfoDao.update(rsg);
                }
            }
        }
    }

    @Override
    public void deletebyLinkId(Integer[] linkIds) {
        linkInfoDao.deletebyLinkId(linkIds);
    }

    @Override
    public List<TimeTaskModel> timedTask(){
        List<LinkInfoEntity> infoEntities = linkInfoDao.selectAll();

        List<TimeTaskModel> timeTaskModels = new ArrayList<TimeTaskModel>();
        Map<String,Integer> flagMap = new HashMap<String,Integer>();

        for(LinkInfoEntity entity : infoEntities){
            if(!flagMap.containsKey(entity.getSystem())){
                TimeTaskModel parent = new TimeTaskModel();
                parent.setValue(entity.getSystem());
                parent.setLabel(entity.getSystem());
                List<TimeTaskModel> children = new ArrayList<TimeTaskModel>();
                parent.setChildren(children);
                flagMap.put(entity.getSystem(),timeTaskModels.size());
                timeTaskModels.add(parent);
            }

            TimeTaskModel child = new TimeTaskModel();
            child.setLabel(entity.getModule());
            child.setValue(String.valueOf(entity.getLinkId()));
            timeTaskModels.get(flagMap.get(entity.getSystem())).getChildren().add(child);
        }
        return timeTaskModels;
    }

    @Override
    public void  updateByTarget(Integer linkId){
        LinkInfoEntity linkInfo = new LinkInfoEntity();
        linkInfo.setLinkId(linkId);
        linkInfo.setHasTarget(Constant.VALUE_ZERO);linkInfoDao.update(linkInfo);
        linkInfoDao.update(linkInfo);
    }

    @Override
    @Transactional
    public void addCookies(Map<String, Object> params){
        LinkInfoEntity linkInfo = new LinkInfoEntity();
        linkInfo.setLinkId(Integer.parseInt(String.valueOf(params.get("linkId"))));
        linkInfo.setHasTarget(Constant.VALUE_ONE);
        List<Map<String,Object>> map = (List<Map<String, Object>>)params.get("cookie");
        //对前端传入的cookie进行组装
        String cookies = "";
        if (map.size() != Constant.VALUE_ZERO){
            for (Map<String,Object> m : map){
                String cookie = "{"+ '\"'+ "name" + '\"'+ ':'+ '\"' +  String.valueOf(m.get("name")) + '\"' + ',' + '\"'+ "value" + '\"'+ ':'+ '\"' +  String.valueOf(m.get("value")) + '\"'+'}' + ',';
                cookies =  cookies + cookie;
            }
            cookies = '[' + cookies.substring(0, cookies.length()-1) + ']';
        }
        AnalogLoginEntity analogLogin = new AnalogLoginEntity();
        analogLogin.setId(analogLoginDao.queryAnalogLoginByLinkId(linkInfo.getLinkId()).getId());
        analogLogin.setHandCookie(cookies.equals("")?null:cookies);
        analogLoginDao.updateHandCookie(analogLogin);
        linkInfoDao.update(linkInfo);
    }

    @Override
    public void  gainCookie(Integer linkId){
        AnalogLoginEntity analogLogin = analogLoginDao.queryAnalogLoginByLinkId(linkId);
        analogLogin.getHandCookie();

    }

}
