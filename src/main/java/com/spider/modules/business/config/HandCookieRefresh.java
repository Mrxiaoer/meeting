package com.spider.modules.business.config;

import com.spider.common.utils.Constant;
import com.spider.modules.business.dao.LinkInfoDao;
import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.spider.config.PhantomJSDriverPool;
import com.spider.modules.spider.core.SpiderPage;
import com.spider.modules.spider.dao.AnalogLoginDao;
import com.spider.modules.spider.entity.AnalogLoginEntity;
import com.spider.modules.spider.pipeline.SpiderTemporaryRecordPipeline;
import com.spider.modules.spider.utils.MyStringUtil;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 实时刷新防止手动输入cookie过期
 *
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2018-09-19
 */
@Component
public class HandCookieRefresh {

    private final int sleepTime = 1500;
    @Autowired
    LinkInfoDao linkInfoDao;
    @Autowired
    AnalogLoginDao analogLoginDao;
    @Autowired
    SpiderPage spiderPage;
    @Autowired
    PhantomJSDriverPool phantomJSDriverPool;
    @Autowired
    SpiderTemporaryRecordPipeline spiderTemporaryRecordPipeline;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0 0 0 0/1 * *")
    public void refreshHandCookie() {

        //查询手动输入cookie的linkinfo信息
        LinkInfoEntity link = new LinkInfoEntity();
        link.setHasTarget(Constant.VALUE_TWO);
        List<LinkInfoEntity> links = linkInfoDao.queryTerm(link);

        //循环刷新页面
        for (LinkInfoEntity value : links) {
            AnalogLoginEntity analog = analogLoginDao.queryAnalogLoginByLinkId(value.getLinkId());
            // 需查看一下cookie空的时候hasTarget状态是否更改,已解决
            if (analog.getHandCookie() != null) {
                Set<Cookie> handcookies = MyStringUtil.json2cookie(analog.getHandCookie());
                logger.info(analog.getHandCookie());
                //将cookie放入模拟器中查看页面是否可达,只测试目标可达问题，不爬取
                //TODO 如何只验证cookie,不进行爬取,待解决
                /*try {
                    PhantomJSDriver driver = phantomJSDriverPool.borrowPhantomJSDriver();
                    try {
                        SpiderClaim spiderClaim = new SpiderClaim();
                        spiderClaim.setCookieSet(handcookies);
                        spiderClaim.setPhantomJSDriver(driver);
                        spiderClaim.setPipeline(spiderTemporaryRecordPipeline);
                        spiderClaim.setSleepTime(2000);

                        SpiderRule spiderRule = new SpiderRule();
                        spiderRule.setIsGetText(false);
                        spiderPage.startSpider(value.getLinkId(), value.getUrl(), spiderClaim, spiderRule);
                    } finally {
                        phantomJSDriverPool.returnObject(driver);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }*/

                //引入SeleniumDownloader
                //TODO domain设置的问题有待解决
                /*PhantomJSDriver driver = null;
                try {
                    driver = phantomJSDriverPool.borrowPhantomJSDriver();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    assert (driver != null);
                    WebDriver.Options manage = driver.manage();
                    logger.info("开始设置cookie");
                    long setCookieStartTime = System.currentTimeMillis();
                    if (handcookies != null) {
                        try {
                            for (Cookie co : handcookies) {
                                manage.addCookie(co);
                            }
                        } catch (Exception e) {
                            driver.get(analog.getTargetUrl());
                            manage.deleteAllCookies();
                            for (Cookie co : handcookies) {
                                manage.addCookie(co);
                            }
                        }
                    }

                    logger.info("设置cookie用时{}毫秒", System.currentTimeMillis() - setCookieStartTime);

                    logger.info("开始下载页面 -- {}", analog.getTargetUrl());
                    long downloadStartTime = System.currentTimeMillis();
                    //			driver.get(request.getUrl());
                    Class[] paramClzs = {String.class};
                    Object[] paramObjs = {analog.getTargetUrl()};
                    int timeOut = 7500;
                    try {
                        //获取目标页，即更新cookie有效性
                        RunTimeout.timeoutMethod(driver, "get", paramClzs, paramObjs, timeOut);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info("当前页 -- {}", driver.getCurrentUrl());
                } finally {
                    try {
                        this.phantomJSDriverPool.returnObject(driver);
                    } catch (Exception e) {
                        try {
                            this.phantomJSDriverPool.invalidateObject(driver);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }*/
            }
        }

    }

}
