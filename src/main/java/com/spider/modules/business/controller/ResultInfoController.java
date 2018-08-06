package com.spider.modules.business.controller;

import java.util.List;
import java.util.Map;

import com.spider.modules.business.dao.PageInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    PageInfoDao pageInfoDao;
    
    @Autowired
    PageInfoService pageInfoService;

    /**
     * 采集结果数据、分页
     * @param (system、null)
     * @return
     */
    @GetMapping("/list")
    public R queryTerm(@RequestParam Map<String,Object> params) {
        PageUtils page = resultInfoService.queryTerm(params);
        return R.ok().put("list",page.getList()).put("page", page);
    }

    @GetMapping("/challenge")
    public R queryChallenge(@RequestParam Map<String,Object> params){
        PageUtils page = resultInfoService.queryChallenge(params);
        return R.ok().put("list",page.getList()).put("page",page);
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
        return R.ok().put("pageInfo", pageInfos);
    }

    /**
     * 采集结果转换=>数据转换
     * @param params
     * @return
     */
    @GetMapping("/datachallege")
    public R itemConversion(@RequestParam Map<String,Object> params){
        List<ResultInfoEntity> resultInfos = resultInfoService.resultByPageInfo(params);
        Integer totalCount = pageInfoDao.querySum(params);
        return R.ok().put("resultInfos",resultInfos).put("totalCount",totalCount);
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
     * 采集结果=>查看=>详情（字段需考虑<暂无>
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
        return R.ok().put("page", page).put("list", page.getList());
    }

    /**
     * 设置模板或者取消模板
     * @param id
     * @return
     */
    @GetMapping("set_template")
    public R setTemplate(@RequestParam Integer id){
        resultInfoService.setTemplate(id);
        return R.ok();
    }

    /**
     * 采集结果比对
     * @param id
     * @return
     */
        @PostMapping("/comparison")
    public R comparison(@RequestBody Integer id) {
        List<PageInfoEntity> pageInfos = resultInfoService.comparison(id);
        return R.ok().put("list",pageInfos);
    }

    /**
     * 获取信息资源详情
     * @param id
     * @return
     */
    @GetMapping("/conversion/{id}")
    public R conversionByInformation(@PathVariable Integer id){
            System.out.println(id);
        List<ResultInfoEntity> results = resultInfoService.conversionByInformation(id);
        return R.ok().put("results",results);
    }
}
