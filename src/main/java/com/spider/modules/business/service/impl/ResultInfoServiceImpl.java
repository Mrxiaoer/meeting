package com.spider.modules.business.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.spider.common.utils.Constant;
import com.spider.common.utils.PageUtils;
import com.spider.common.utils.Query;
import com.spider.modules.business.dao.ResultInfoDao;
import com.spider.modules.business.entity.PageInfoEntity;
import com.spider.modules.business.entity.ResultInfoEntity;
import com.spider.modules.business.model.ResultInfoModel;
import com.spider.modules.business.service.PageInfoService;
import com.spider.modules.business.service.ResultInfoService;

@Service
public class ResultInfoServiceImpl extends ServiceImpl<ResultInfoDao, ResultInfoModel> implements ResultInfoService {            
    @Autowired
    ResultInfoDao resultInfoDao;
    
    @Autowired
    PageInfoService pageInfoService;

    @Override
    public PageUtils queryTerm(Map<String,Object> params) {
        String system = (String) params.get("system");
        Page<ResultInfoModel> page = this.selectPage(new Query<ResultInfoModel>(params).getPage(), 
                            new EntityWrapper<ResultInfoModel>().like(StringUtils.isNotBlank(system), "system",system)
                            .eq("state", Constant.TRUE_STATE));
        return new PageUtils(page);
    }
    
    @Override
    public PageUtils queryModel(Map<String, Object> params) {
        Page<ResultInfoModel> page = this.selectPage(new Query<ResultInfoModel>(params).getPage(), 
                        new EntityWrapper<ResultInfoModel>().eq("state", Constant.SUPER_ADMIN)
                        .and().eq("is_model", Constant.SUPER_ADMIN));
        return new  PageUtils(page); 
    }

    
    @Override
    public List<ResultInfoEntity> queryByResultId(Integer resultId) {
        return resultInfoDao.queryByResultId(resultId);
    }
    
    @Override
    public List<Map<String, Object>> querySum() {
        return resultInfoDao.querySum();
    }
    
    public void save(ResultInfoEntity resultInfo) {
        resultInfoDao.insert(resultInfo);
    }

    
    @Override
    public Map<String,Object> comparison(Integer currentId,Integer modelId) {
        
        List<PageInfoEntity> currentDataList = pageInfoService.queryByResultId(currentId);
        List<PageInfoEntity> lastDataList = pageInfoService.queryByResultId(modelId);
        Map<String,Object> map = new HashMap<>();
        map.put("currentDataList", currentDataList);
        map.put("lastDataList", lastDataList);
        return map;
    }



}
