package com.spider.modules.business.service;

import java.util.Map;

import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.business.model.TargetInfoEntity;
import com.spider.modules.spider.entity.TemporaryRecordEntity;

public interface TargetInfoService {

    
    /**
     * 模拟登录第一步
     * @param linkId
     * @return
     */
    String tospider(Integer linkId);
    
    /**
     * 根据条件(用户名，密码）更新数据
     * 并模拟查询插入cookie
     * author yaonuan-7.17
     * @param analogLogin
     * @return
     */
    LinkInfoEntity update(TargetInfoEntity targetInfo,Integer analogId);

    /**
     * 单点采集，采集表头
     * 返回给前，选择要爬取得部分
     * @param analogId
     */
    TemporaryRecordEntity tothirdspider(Integer linkId);
    
    /**
     * 获取xpath给前端返回采集的表头
     * @param params
     * @return
     */
    Map<String,Object> getXpath(Map<String,Object> params);
    
    /**
     * 用户保存爬取页信息
     * @param params
     */
    void updateHead(Map<String,Object> params);
}
