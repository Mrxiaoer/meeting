package com.spider.modules.business.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spider.common.utils.PageUtils;
import com.spider.common.utils.R;
import com.spider.modules.business.entity.ElementInfoEntity;
import com.spider.modules.business.service.ElementInfoService;
/**
 * 采集结果转化存储表
 * @author yaonuan
 * @data 2018年7月11日
 * version 1.0
 */
@RestController
@RequestMapping(value = "/api/element")
public class ElementInfoController {
    
    @Autowired
    ElementInfoService elementInfoService;
    
    /**
     * 根据自定义条件(中文名)查询数据
     * 默认传参数information_name
     * @param params
     * @return
     */
    @PostMapping("list")
    public R queryTerm(@RequestBody Map<String,Object> params){
        PageUtils page = elementInfoService.queryTerm(params);
        return R.ok().put("totalCount",page.getTotalCount()).put("pageSize", page.getPageSize())
                .put("totalPage", page.getTotalPage()).put("currPage", page.getCurrPage())
                .put("list", page.getList());
    }
    
    /**
     * 根据element_id查询数据
     * @param elementId
     * @return
     */
    @GetMapping("/info/{elementId}")
    public R selectById(@PathVariable("elementId") Integer elementId){
        ElementInfoEntity elementInfo = elementInfoService.queryById(elementId);
        return R.ok().put("elementInfo", elementInfo);
       
    }
    
    /**
     * 更新数据
     * @param elementInfo
     * @return
     */
    @PostMapping("/update")
    public R update(@RequestBody ElementInfoEntity elementInfo){
        elementInfoService.update(elementInfo);
        return R.ok();
    }
    
    /**
     * 数据逻辑删除，根据element_id
     * @param elementIds
     * @return
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Integer[] elementIds){
        elementInfoService.delete(elementIds);
        return R.ok();
    }
}
