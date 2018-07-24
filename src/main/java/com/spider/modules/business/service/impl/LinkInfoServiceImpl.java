package com.spider.modules.business.service.impl;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.spider.common.utils.Constant;
import com.spider.common.utils.PageUtils;
import com.spider.common.utils.Query;
import com.spider.modules.business.dao.LinkInfoDao;
import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.business.service.LinkInfoService;
import com.spider.modules.spider.dao.AnalogLoginDao;
import com.spider.modules.spider.entity.AnalogLoginEntity;
import com.spider.modules.spider.service.AnalogLoginService;

/**
 * 
 * <p> title: LinkInfoServiceImpl </p> 
 * @author yaonuan
 * @data 2018年7月9日
 * version 1.0
 */
@Service
public class LinkInfoServiceImpl extends ServiceImpl<LinkInfoDao, LinkInfoEntity>  implements LinkInfoService {

    @Autowired
    LinkInfoDao linkInfoDao;
    
    @Autowired
    AnalogLoginService analogLoginService;
    
    @Autowired
    AnalogLoginDao analogLoginDao;
    
    @Override
    public PageUtils queryTerm(Map<String, Object> params) {
        String system = (String) params.get("system");
        String module = (String) params.get("module");
        Page<LinkInfoEntity> page = this.selectPage(new Query<LinkInfoEntity>(params).getPage(), 
                new EntityWrapper<LinkInfoEntity>().like(StringUtils.isNotBlank(system),"system" ,system)
                .and().like(StringUtils.isNotBlank(module),"module" ,module).eq("state", Constant.TRUE_STATE));
        return new PageUtils(page);
    }
    
    @Override
    public LinkInfoEntity queryById(Integer linkId) {
        return linkInfoDao.queryById(linkId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Map<String,Object> params) {
        AnalogLoginEntity analogLogin = new AnalogLoginEntity();
        
        //在AnalogLoginEntity表中存用户爬取的url
        analogLogin.setTargetUrl((String) params.get("url"));
        if(analogLogin.getId() !=null) {
            analogLogin.setId(null);
        }
        analogLoginService.saveAnalogLogin(analogLogin);
        
        
        LinkInfoEntity linkInfo = new LinkInfoEntity();
        linkInfo.setAnalogId(analogLogin.getId());
        System.err.println(analogLogin.getId());
        linkInfo.setSystem((String) params.get("system"));
        linkInfo.setModule((String) params.get("mpdule"));
        linkInfo.setUrl((String) params.get("url"));
        linkInfo.setIsLogin((Integer) params.get("isLogin"));
        linkInfo.setRemarks((String) params.get("remarks"));
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
        linkInfoDao.update(linkInfo);
        if(linkInfo.getUrl() != null) {
           AnalogLoginEntity rc = new AnalogLoginEntity();
           Integer id = this.queryById(linkInfo.getLinkId()).getAnalogId();
           rc.setId(id);
           rc.setTargetUrl(linkInfo.getUrl());
           analogLoginDao.updateAnalogLogin(rc);
        }
    }

    @Override
    public void deletebyLinkId(Integer[] linkIds) {
        linkInfoDao.deletebyLinkId(linkIds);
    }


}
