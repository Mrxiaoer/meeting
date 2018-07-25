package com.spider.modules.business.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spider.common.utils.PageUtils;
import com.spider.common.utils.R;
import com.spider.modules.business.entity.PageInfoEntity;
import com.spider.modules.business.entity.ResultInfoEntity;
import com.spider.modules.business.service.PageInfoService;
import com.spider.modules.business.service.ResultInfoService;

/**
 * 采集结果数据
 * @author yaonuan
 * version 1.0
 */
@RestController
@RequestMapping("/api/resultInfo")
public class ResultInfoController {
    
    @Autowired
    ResultInfoService resultInfoService;
    
    @Autowired
    PageInfoService pageInfoService;
    
    /**
     * 采集结果数据、分页
     * @param 可根据系统名查看
     * @return
     */
    @GetMapping("/list")
    public R queryTerm(@RequestParam Map<String,Object> params) {
        PageUtils page = resultInfoService.queryTerm(params);
        return R.ok().put("totalCount",page.getTotalCount()).put("pageSize", page.getPageSize())
                .put("totalPage", page.getTotalPage()).put("currPage", page.getCurrPage())
                .put("list", page.getList());
    }
    
    /**
     * 采集结果的详细信息
     * 采集结果=>查看
     * @param resultId
     * @return
     */
    @GetMapping("/query_result_id")
    public R queryByResultId(@RequestParam Integer resultId) {
        List<PageInfoEntity> pageInfos = pageInfoService.queryByResultId(resultId);
        return R.ok().put("pageInfos", pageInfos);
    }
    
    /**
     * 资源目录
     * @return
     */
    @GetMapping("/catalog")
    public R querySum() {
        List<Map<String, Object>> maps =  resultInfoService.querySum();
        return R.ok().put("maps", maps);
    }
    
    /**
     * 资源目录=>查看
     * @param resultId
     * @return
     */
    @GetMapping("/result_list")
    public R resulrList(@RequestParam Integer resultId) {
        List<ResultInfoEntity> resultInfos =  resultInfoService.queryByResultId(resultId);
        return R.ok().put("resultInfos", resultInfos);
    }
    
    /**
     * 采集结果=>查看=>详情（字段需考虑）
     * @param pageId
     * @return
     */
    @GetMapping("/result_page_id")
    public R queryByPageId(@RequestParam Integer pageId) {
        PageInfoEntity pageInfo = pageInfoService.queryByPageId(pageId);
        return R.ok().put("pageInfo", pageInfo);
    }

    /**
           * 查看模板
     * @return
     */
    @GetMapping("/is_model")
    public R seeModel(@RequestParam Map<String,Object> params) {
        PageUtils page = resultInfoService.queryModel(params);
        return R.ok().put("totalCount",page.getTotalCount()).put("pageSize", page.getPageSize())
                .put("totalPage", page.getTotalPage()).put("currPage", page.getCurrPage())
                .put("list", page.getList());
    }
    
    /**
         * 比对
     * @param resultId
     * @return
     */
    @GetMapping("/comparison")
    public R comparison(@RequestParam("currentId") Integer currentId,
                        @RequestParam("modelId") Integer modelId) {
        Map<String, Object> map = resultInfoService.comparison(currentId, modelId);
        return R.ok().put("lastDataList", map.get("lastDataList")).put("currentDataList", map.get("currentDataList"));
    }
}
