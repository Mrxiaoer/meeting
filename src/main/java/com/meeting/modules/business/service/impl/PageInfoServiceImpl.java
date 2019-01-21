package com.meeting.modules.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.meeting.common.utils.Constant;
import com.meeting.common.utils.PageUtils;
import com.meeting.common.utils.Query;
import com.meeting.modules.business.dao.PageInfoDao;
import com.meeting.modules.business.dao.ResultInfoDao;
import com.meeting.modules.business.entity.ElementInfoEntity;
import com.meeting.modules.business.entity.PageInfoEntity;
import com.meeting.modules.business.entity.ResultInfoEntity;
import com.meeting.modules.business.model.ProvisionalEntity;
import com.meeting.modules.business.service.ElementInfoService;
import com.meeting.modules.business.service.PageInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PageInfoServiceImpl extends ServiceImpl<PageInfoDao, PageInfoEntity> implements PageInfoService {

    @Autowired
    PageInfoDao pageInfoDao;

    @Autowired
    ResultInfoDao resultInfoDao;

    @Autowired
    ElementInfoService elementInfoService;
    
    @Override
    public List<PageInfoEntity> queryByResultId(Integer resultId) {
        return pageInfoDao.queryByResultId(resultId);
    }

    @Override
    public PageUtils queryTerm(Map<String,Object> params){
        String state = Constant.TRUE_STATE;
        String nameCn = (String) params.get("nameCn");
        Integer resultId = Integer.parseInt(String.valueOf(params.get("resultId")));
        Page<PageInfoEntity> page = this.selectPage(new Query<PageInfoEntity>(params).getPage(),
                new EntityWrapper<PageInfoEntity>().like(StringUtils.isNotBlank(nameCn), "name_cn", nameCn)
                        .eq("state", state).and().eq("result_id", resultId));
        return new PageUtils(page);
    }

    @Override
    public List<PageInfoEntity> listByResultId(Integer resultId){
        return pageInfoDao.listByResultId(resultId);
    }

    @Override
    public PageInfoEntity queryById(Integer pageId) {
        return pageInfoDao.queryById(pageId);
    }

    @Override
    public PageInfoEntity queryByPageId(Integer pageId) {
        return pageInfoDao.queryByPageId(pageId);
    }
    
    @Override
    public void update(PageInfoEntity pageInfo) {
        pageInfo.setUpdateTime(new Date());
        pageInfoDao.update(pageInfo);
    }

    @Override
    public void save(PageInfoEntity pageInfo) {
        if(pageInfo.getPageId() != null) {
            pageInfo.setPageId(null);
        }
        pageInfo.setCreateTime(new Date());
        pageInfo.setUpdateTime(new Date());
        pageInfoDao.insert(pageInfo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePageAndElement(ProvisionalEntity provisional) {
        PageInfoEntity pageInfo =  new PageInfoEntity();
        ElementInfoEntity elementInfo = new ElementInfoEntity();
        BeanUtil.copyProperties(provisional, pageInfo);
        pageInfo.setIsChange(Constant.SUPER_ADMIN);
        BeanUtil.copyProperties(provisional, elementInfo);
        this.update(pageInfo);
        elementInfoService.save(elementInfo);

        List<PageInfoEntity> pageInfos = this.queryByResultId(provisional.getResultId());
        //在结果表中判断是否已经转化完成
        ResultInfoEntity rs = new ResultInfoEntity();
        rs.setId(provisional.getResultId());
        if(pageInfos.size() == Constant.VALUE_ZERO){
            rs.setChangeState(Constant.VALUE_TWO);
            resultInfoDao.update(rs);
        }else {
            rs.setChangeState(Constant.SUPER_ADMIN);
            resultInfoDao.update(rs);
        }
    }

    @Override
    public void delete(Integer[] pageIds){
        pageInfoDao.delete(pageIds);
    }

}
