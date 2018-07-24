package com.spider.modules.business.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spider.common.utils.R;
import com.spider.modules.business.model.ProvisionalEntity;
import com.spider.modules.business.service.PageInfoService;


@RestController
@RequestMapping("/api/pageInfo")
public class PageInfoController {

    @Autowired
    PageInfoService pageInfoService;
    
    /**
     * 信息转化
     * @param provisional
     * @return
     */
    @PutMapping("/chang_data")
    public R update(@RequestBody ProvisionalEntity provisional) 
    {
        pageInfoService.updatePageAndElement(provisional);
        return R.ok();
    }
}
