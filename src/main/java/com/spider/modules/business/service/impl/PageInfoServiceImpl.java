package com.spider.modules.business.service.impl;

import java.util.Date;
import java.util.List;

import com.spider.modules.business.dao.ResultInfoDao;
import com.spider.modules.business.entity.ResultInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.spider.common.utils.Constant;
import com.spider.modules.business.dao.PageInfoDao;
import com.spider.modules.business.entity.ElementInfoEntity;
import com.spider.modules.business.entity.PageInfoEntity;
import com.spider.modules.business.model.ProvisionalEntity;
import com.spider.modules.business.service.ElementInfoService;
import com.spider.modules.business.service.PageInfoService;

import cn.hutool.core.bean.BeanUtil;

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
    public List<PageInfoEntity> listByResultId(Integer resultId){
        return pageInfoDao.listByResultId(resultId);
    }

    @Override
    public PageInfoEntity queryById(Integer pageId) {
        PageInfoEntity pageInfo = pageInfoDao.queryById(pageId);
        return pageInfo;
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
        if(pageInfos.size() == 0){
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
