package com.meeting.modules.business.controller;


import com.meeting.common.utils.PageUtils;
import com.meeting.common.utils.R;
import com.meeting.modules.business.entity.PageInfoEntity;
import com.meeting.modules.business.model.ProvisionalEntity;
import com.meeting.modules.business.service.PageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    //想渲染前端比对的查看，未更换
    @GetMapping("/list")
    public R queryTerm(@RequestParam Map<String,Object> params){
        PageUtils pager = pageInfoService.queryTerm(params);
        return R.ok();
    }

    @GetMapping("template_details/{id}")
    public R listByResultId(@PathVariable("id") Integer resultId){
        List<PageInfoEntity> pageInfos = pageInfoService.listByResultId(resultId);
        return R.ok().put("list", pageInfos);
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

    @PostMapping("/delete")
    public R delete(@RequestBody Integer[] pageIds){
        pageInfoService.delete(pageIds);
        return  R.ok();
    }

}
