package com.meeting.modules.business.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meeting.common.utils.PageUtils;
import com.meeting.common.utils.R;
import com.meeting.modules.business.entity.ElementInfoEntity;
import com.meeting.modules.business.service.ElementInfoService;
/**
 * 采集结果转化表
 * @author yaonuan
 * @data 2018年7月11日
 */
@RestController
@RequestMapping(value = "/api/element")
public class ElementInfoController {
    
    @Autowired
    ElementInfoService elementInfoService;
    
    /**
     * 根据自定义条件(中文名)查询数据
     * @param params(page、limit、中文名)
     * @return 分页信息、数据
     */
    @GetMapping("/list")
    public R queryTerm(@RequestParam Map<String,Object> params){
        PageUtils page = elementInfoService.queryTerm(params);
        return R.ok().put("page", page).put("list", page.getList());
    }
    
    /**
     * 根据eid查询对应数据
     * @param elementId
     * @return
     */
    @GetMapping("/info/{elementId}")
    public R selectById(@PathVariable("elementId") Integer elementId){
        ElementInfoEntity elementInfo = elementInfoService.queryById(elementId);
        return R.ok().put("list", elementInfo);
       
    }

    /**
     * 根据pageId查询elementInfo
     * @param pageId
     * @return
     */
    @GetMapping("/queryByPageId/{pageId}")
    public R selectByPageId(@PathVariable Integer pageId){
        ElementInfoEntity elementInfo = elementInfoService.selectByPageId(pageId);
        return R.ok().put("list", elementInfo);
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
