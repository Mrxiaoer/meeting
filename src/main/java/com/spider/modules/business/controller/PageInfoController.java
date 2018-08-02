package com.spider.modules.business.controller;


import com.spider.modules.business.entity.PageInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.spider.common.utils.R;
import com.spider.modules.business.model.ProvisionalEntity;
import com.spider.modules.business.service.PageInfoService;

/**
 * 采集结果表
 * @author yaonuan
 * @data 2018年7月24日
 */
@RestController
@RequestMapping("/api/pageInfo")
public class PageInfoController {

    @Autowired
    PageInfoService pageInfoService;

    @GetMapping("/query_id/{id}")
    public R queryById(@PathVariable Integer id){
        PageInfoEntity pageInfo = pageInfoService.queryById(id);
        return R.ok().put("list",pageInfo);
    }

    /**
     * 信息转化
     * @param provisional
     * @return
     */
    @PostMapping("/chang_data")
    public R update(@RequestBody ProvisionalEntity provisional) 
    {
        pageInfoService.updatePageAndElement(provisional);
        return R.ok();
    }
}
