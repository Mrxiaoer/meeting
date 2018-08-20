package com.spider.modules.business.service.impl;

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
import com.spider.modules.business.dao.ElementInfoDao;
import com.spider.modules.business.entity.ElementInfoEntity;
import com.spider.modules.business.service.ElementInfoService;

@Service
public class ElementInfoServiceImpl extends ServiceImpl<ElementInfoDao, ElementInfoEntity> implements ElementInfoService {

    @Autowired
    ElementInfoDao elementInfoDao;

    @Override
    public PageUtils queryTerm(Map<String, Object> params) {
        String state = Constant.TRUE_STATE;
        String nameCn = (String) params.get("nameCn");
        Integer resultId = Integer.parseInt(String.valueOf(params.get("resultId")));
        Page<ElementInfoEntity> page = this.selectPage(new Query<ElementInfoEntity>(params).getPage(),
                new EntityWrapper<ElementInfoEntity>().like(StringUtils.isNotBlank(nameCn), "name_cn", nameCn)
                        .eq("state", state).and().eq("result_id", resultId));
        return new PageUtils(page);
    }

    @Override
    public ElementInfoEntity queryById(Integer elementId) {
        return elementInfoDao.queryById(elementId);
    }

    @Override
    public ElementInfoEntity selectByPageId(Integer pageId){
        ElementInfoEntity elementInfo = new ElementInfoEntity();
        elementInfo.setPageId(pageId);
        return  elementInfoDao.selectOne(elementInfo);
    }
    @Override
    public void save(ElementInfoEntity elementInfo) {
        elementInfoDao.insert(elementInfo);
    }

    @Override
    public int update(ElementInfoEntity elementInfo) {
        return elementInfoDao.update(elementInfo);
    }

    @Override
    public void delete(Integer[] elementIds) {
        elementInfoDao.delete(elementIds);
    }


}
