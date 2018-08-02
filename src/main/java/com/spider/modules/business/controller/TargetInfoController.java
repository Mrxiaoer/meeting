package com.spider.modules.business.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spider.common.utils.R;
import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.business.model.TargetInfoEntity;
import com.spider.modules.business.service.LinkInfoService;
import com.spider.modules.business.service.TargetInfoService;
import com.spider.modules.spider.entity.TemporaryRecordEntity;
import com.spider.modules.spider.service.AnalogLoginService;
import com.spider.modules.spider.service.TemporaryRecordService;

import us.codecraft.webmagic.selector.Html;
/**
 * 采集页
 * @author yaonuan
 * @data 2018年7月20日
 * version 1.0
 */
@RestController
@RequestMapping("/api/target")
public class TargetInfoController {

    @Autowired
    LinkInfoService linkInfoService;
    
    @Autowired
    AnalogLoginService analogLoginService;
    
    @Autowired
    TemporaryRecordService temporaryRecordService;
    
    @Autowired
    TargetInfoService targetInfoService;
     

    /**
     * 采集登录页的Html
     * @param linkId
     * @return
     */
    @GetMapping("/analog_login_one")
    public R tospider(@RequestParam("id") Integer linkId) {
        String html = targetInfoService.tospider(linkId);
        Html content =  Html.create(html);
        return  R.ok().put("contents", html);
    }
    
    /**
     * 模拟登录
     * @param （username，password，4个xpath+验证码url）
     * @return
     */
    @PostMapping("/analog_login_two")
    public R update(@RequestBody TargetInfoEntity targetInfo) {
        LinkInfoEntity linkInfoone = linkInfoService.queryById(targetInfo.getLinkId());
        Integer analogId = linkInfoone.getAnalogId();
        targetInfo.setUrl(linkInfoone.getUrl());
        LinkInfoEntity linkInfo = targetInfoService.update(targetInfo,analogId);
        return R.ok().put("linkInfo", linkInfo);
    }
    
    /**
     * 单点采集采集页面，返回
     * @param linkId
     * @return
     */
    @GetMapping("/spdier_point")
    public R getOneById(@RequestParam Integer linkId) {
       TemporaryRecordEntity temporary = targetInfoService.tothirdspider(linkId);
        return R.ok().put("url", temporary.getUrl()).put("contents",temporary.getHtml());
        
    }
    
    /**
     * 前：表头xpath、linkId
     * @param params
     * @return 采集到的表头信息list
     */
    @PostMapping("/spider_head")
    public R getxpath(@RequestBody Map<String,Object> params) {
        Map<String,Object> map = new HashMap<String,Object>();
        map = targetInfoService.getXpath(params);
        return R.ok().put("pageInfos", map.get("pageInfos"));
                
    }
    
    /**
     * 保存采集到的数据
     * @param
     * @return
     */
    @PostMapping("/spider_head_update")
    public R updateHead(@RequestBody Map<String,Object> params) {
        targetInfoService.updateHead(params);
        return R.ok();
    }

}
