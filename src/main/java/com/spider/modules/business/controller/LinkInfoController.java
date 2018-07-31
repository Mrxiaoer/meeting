package com.spider.modules.business.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spider.common.utils.PageUtils;
import com.spider.common.utils.R;
import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.business.service.LinkInfoService;
import com.spider.modules.spider.service.AnalogLoginService;

/**
 * 站点爬取链接配置
 * 链接信息表controller类
 * 前端使用json发送数据给后端
 * <p> title: LinkInfoController </p> 
 * @author yaonuan
 * @data 2018年7月9日
 * version 1.0
 */
@RestController
@RequestMapping(value = "/api/linkInfo")
public class LinkInfoController {
    
    @Autowired
    LinkInfoService linkInfoService;
    
    @Autowired
    AnalogLoginService analogLoginService;

    /**
     * 根据(系统名称、模块名称)条件进行查询
     * 默认空属性时，查询所有
     * @param linkInfoEntity
     * @return
     */
    @GetMapping("/list")
    public R queryTerm(@RequestParam Map<String,Object> params){
        PageUtils page = linkInfoService.queryTerm(params);
        return R.ok().put("page", page).put("list", page.getList());
    }
    
    /**
     * 根据linkId查询信息
     * @param linkId
     * @return
     */
    @GetMapping("/info/{linkId}")
    public R selectByPrimaryKey(@PathVariable("linkId") Integer linkId){
        LinkInfoEntity linkInfo = linkInfoService.queryById(linkId);
        return R.ok().put("list", linkInfo);
    }
    
    /**
     * 插入数据，先对analog_login进行操作
     * @return
     */
    @PostMapping("/save")
    public R insert(@RequestBody LinkInfoEntity linkInfo){
        
        linkInfoService.save(linkInfo);
        return R.ok();
    }
    
    /**
     * updata 更新数据
     * @param linkInfo
     * @return
     */
    @PostMapping("/update")
    public R update(@RequestBody LinkInfoEntity linkInfo){
        linkInfoService.update(linkInfo);
        return R.ok();
    }
    
    /**
     * 删除数据，逻辑删除
     * @param linkIds
     * @return
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Integer[] linkIds){
        linkInfoService.deletebyLinkId(linkIds);
        return R.ok();
    }

}
