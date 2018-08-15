package com.spider.modules.business.controller;

import com.spider.common.utils.R;
import com.spider.modules.business.model.PageCountModel;
import com.spider.modules.business.service.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 首页controller
 *
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2018-08-13
 */
@RestController
@RequestMapping("/api/home")
public class HomePageController {

    @Autowired
    HomePageService homePageService;

    @GetMapping("/getInfoCount")
    public R getInfoCount(){
        List<PageCountModel> count = homePageService.getInfoCount();
        return R.ok().put("list", count);
    }

    @GetMapping("/getTrendData")
    public R getTrendData(){
        Map<String,Object> map = homePageService.getTrendData();
        return R.ok().put("list", map);
    }
}
