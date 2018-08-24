package com.spider.modules.business.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.spider.common.utils.Constant;
import com.spider.common.utils.PageUtils;
import com.spider.common.utils.Query;
import com.spider.modules.business.dao.PageInfoDao;
import com.spider.modules.business.dao.ResultInfoDao;
import com.spider.modules.business.entity.PageInfoEntity;
import com.spider.modules.business.entity.ResultInfoEntity;
import com.spider.modules.business.model.ResultInfoModel;
import com.spider.modules.business.service.PageInfoService;
import com.spider.modules.business.service.ResultInfoService;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultInfoServiceImpl extends ServiceImpl<ResultInfoDao, ResultInfoModel> implements ResultInfoService {            
    @Autowired
    ResultInfoDao resultInfoDao;

    @Autowired
    PageInfoDao pageInfoDao;

    @Autowired
    PageInfoService pageInfoService;

    @Override
    public PageUtils queryTerm(Map<String,Object> params) {
        String system = (String) params.get("system");
        Page<ResultInfoModel> page = this.selectPage(new Query<ResultInfoModel>(params).getPage(),
                            new EntityWrapper<ResultInfoModel>().like(StringUtils.isNotBlank(system), "system",system)
                            .eq("state", Constant.TRUE_STATE).orderBy("id",false));
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryChallenge(Map<String,Object> params){
        String system = (String) params.get("system");
        String module = (String) params.get("module");
        String changeState = String.valueOf(params.get("changeState"));
        if(changeState != null && !"".equals(changeState) && !"null".equals(changeState)){
            Integer change = Integer.valueOf(changeState);
            Page<ResultInfoModel> page = this.selectPage(new Query<ResultInfoModel>(params).getPage(),
                    new EntityWrapper<ResultInfoModel>().like(StringUtils.isNotBlank(system), "system",system)
                            .like(StringUtils.isNotBlank(module),"module",module).eq("change_state",change)
                            .eq("state", Constant.TRUE_STATE).orderBy("id",false));
            return new PageUtils(page);
        }else{
            Page<ResultInfoModel> page = this.selectPage(new Query<ResultInfoModel>(params).getPage(),
                    new EntityWrapper<ResultInfoModel>().like(StringUtils.isNotBlank(system), "system",system)
                            .like(StringUtils.isNotBlank(module),"module",module)
                            .eq("state", Constant.TRUE_STATE).orderBy("id",false));
            return new PageUtils(page);
        }
    }

    @Override
    public PageUtils queryModel(Map<String, Object> params) {
        Page<ResultInfoModel> page = this.selectPage(new Query<ResultInfoModel>(params).getPage(), 
                        new EntityWrapper<ResultInfoModel>().eq("state", Constant.SUPER_ADMIN)
                        .and().eq("is_model", Constant.SUPER_ADMIN).orderBy("id",false));
        return new  PageUtils(page); 
    }

    
    @Override
    public List<ResultInfoEntity> queryByResultId(Integer resultId) {
        return resultInfoDao.queryByResultId(resultId);
    }

    @Override
    public  List<ResultInfoEntity> conversionByInformation(Integer linkId){
        List<ResultInfoEntity> resultIns = resultInfoDao.conversionByInformation(linkId);
        return  resultIns;
    }

    @Override
    public List<Map<String, Object>> querySum() {
        return resultInfoDao.querySum();
    }

    @Override
    public void save(ResultInfoEntity resultInfo) {
        resultInfoDao.insert(resultInfo);
    }

    @Override
    public  void setTemplate(Integer id){
        ResultInfoEntity rs = resultInfoDao.queryById(id);
        ResultInfoEntity qs = new ResultInfoEntity();
        qs.setId(id);
        System.out.println(rs.getIsModel());
        if(rs.getIsModel() == Constant.VALUE_ZERO){
            qs.setIsModel(Constant.SUPER_ADMIN);
        }else{
            qs.setIsModel(Constant.VALUE_ZERO);
        }
        resultInfoDao.update(qs);
    }
    
    @Override
    public List<PageInfoEntity> comparison(Integer id) {
        List<PageInfoEntity> pageInfos = pageInfoService.listByResultId(id);
        return pageInfos;
    }

    @Override
    public  List<ResultInfoEntity> resultByPageInfo(Map<String,Object> map){
        List<ResultInfoEntity> resultInfos = resultInfoDao.listByResultId(map);
        return  resultInfos;
    }
}
