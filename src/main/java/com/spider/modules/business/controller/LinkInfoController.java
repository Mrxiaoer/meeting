package com.spider.modules.business.controller;

import com.spider.common.utils.Constant;
import com.spider.common.utils.PageUtils;
import com.spider.common.utils.R;
import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.business.model.TimeTaskModel;
import com.spider.modules.business.service.LinkInfoService;
import com.spider.modules.spider.service.AnalogLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 站点爬取链接配置
 * 链接信息表controller类
 * 前端使用json发送数据给后端
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

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 根据(系统名称、模块名称)条件进行查询
     * @param params
     * @return
     */
    @GetMapping("/list")
    public R queryTerm(@RequestParam Map<String,Object> params){
        PageUtils page = linkInfoService.queryTerm(params);
        return R.ok().put("page", page).put("list", page.getList());
    }

    /**
     * 资源目录精准查询
     * @param params
     * @return
     */
    @GetMapping("/query_accurate")
    public R queryAccurate(@RequestParam Map<String,Object> params){
        PageUtils page = linkInfoService.queryAccurate(params);
        return R.ok().put("page",page).put("list",page.getList());
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
     * 插入数据，对analog与linkInfo进行操作
     * @param linkInfo
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
        if (linkInfo.getIsLogin() == Constant.VALUE_ONE){
            linkInfo.setHasTarget(Constant.VALUE_ZERO);
        }else{
            linkInfo.setHasTarget(Constant.VALUE_ONE);
        }
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

    /**
     * 树结构，查询系统名、模块名
     * @return
     */
    @GetMapping("/tree-structured")
    public R getModel(){
        List<TimeTaskModel> list = linkInfoService.timedTask();
        return R.ok().put("list",list);
    }

    @PostMapping("/hand_cookie")
    public R manualAddCookie(@RequestBody Map<String,Object> params){
        linkInfoService.addCookies(params);
        return R.ok();
    }

    @GetMapping("/gain_cookie/{linkId}")
    public R gainCookie(@PathVariable Integer linkId){
        List<Map<String,String>> cookie = linkInfoService.gainCookie(linkId);
        return  R.ok().put("cookie", cookie).put("length", cookie.size());
    }

}
