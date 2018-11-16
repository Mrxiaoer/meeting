package com.spider.modules.job.task;

import com.spider.common.utils.Constant;
import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.business.entity.PageInfoEntity;
import com.spider.modules.business.entity.ResultInfoEntity;
import com.spider.modules.business.service.LinkInfoService;
import com.spider.modules.business.service.PageInfoService;
import com.spider.modules.business.service.ResultInfoService;
import com.spider.modules.spider.config.PhantomJSDriverPool;
import com.spider.modules.spider.core.HtmlProcess;
import com.spider.modules.spider.core.LoginAnalog;
import com.spider.modules.spider.core.SpiderPage;
import com.spider.modules.spider.entity.AnalogLoginEntity;
import com.spider.modules.spider.entity.SpiderClaim;
import com.spider.modules.spider.entity.SpiderRule;
import com.spider.modules.spider.entity.TemporaryRecordEntity;
import com.spider.modules.spider.pipeline.SpiderTemporaryRecordPipeline;
import com.spider.modules.spider.service.AnalogLoginService;
import com.spider.modules.spider.service.SpiderRuleService;
import com.spider.modules.spider.service.TemporaryRecordService;
import com.spider.modules.spider.utils.MyStringUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自动爬取定时任务
 * <p>
 * autoTask为spring bean的名称
 *
 * @author maoxinmin
 */
@Component("autoTask")
public class AutoTask {

    private final TemporaryRecordService temporaryRecordService;
    private final LinkInfoService linkInfoService;
    private final SpiderRuleService spiderRuleService;
    private final HtmlProcess htmlprocess;
    private final ResultInfoService resultInfoService;
    private final PageInfoService pageInfoService;
    private final PhantomJSDriverPool phantomJSDriverPool;
    private final AnalogLoginService analogLoginService;
    private final LoginAnalog loginAnalog;
    private final SpiderTemporaryRecordPipeline spiderTemporaryRecordPipeline;
    private final SpiderPage spiderPage;
    private final HtmlProcess htmlProcess;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public AutoTask(AnalogLoginService analogLoginService, LoginAnalog loginAnalog,
            SpiderTemporaryRecordPipeline spiderTemporaryRecordPipeline, SpiderPage spiderPage, HtmlProcess htmlProcess,
            TemporaryRecordService temporaryRecordService, LinkInfoService linkInfoService,
            SpiderRuleService spiderRuleService, HtmlProcess htmlprocess, ResultInfoService resultInfoService,
            PageInfoService pageInfoService, PhantomJSDriverPool phantomJSDriverPool) {
        this.analogLoginService = analogLoginService;
        this.loginAnalog = loginAnalog;
        this.spiderTemporaryRecordPipeline = spiderTemporaryRecordPipeline;
        this.spiderPage = spiderPage;
        this.htmlProcess = htmlProcess;
        this.temporaryRecordService = temporaryRecordService;
        this.linkInfoService = linkInfoService;
        this.spiderRuleService = spiderRuleService;
        this.htmlprocess = htmlprocess;
        this.resultInfoService = resultInfoService;
        this.pageInfoService = pageInfoService;
        this.phantomJSDriverPool = phantomJSDriverPool;
    }

    public void autoCrawl(String params) throws Exception {
        logger.info("自动爬取方法autoCrawl正在被执行，站点参数为：" + params);

        String isNum = "^[0-9]*$";
        AnalogLoginEntity loginInfo;
        //根据linkId(params)查询analog_login表对应的信息
        if (params.matches(isNum)) {
            loginInfo = analogLoginService.getOneByLinkId(Integer.parseInt(params));
        } else {
            throw new Exception("定时任务参数异常！");
        }
        LinkInfoEntity linkInfoEntity = linkInfoService.queryById(Integer.parseInt(params));
        if (linkInfoEntity.getHasTarget() == Constant.VALUE_ONE) {
            //根据定时任务中的params
            AnalogLoginEntity analogLogin = analogLoginService.getOneByLinkId(linkInfoEntity.getLinkId());
            //采用数据库中cookie尝试登录
            Set<Cookie> cookies;

            int linkId = Integer.parseInt(params);
            SpiderRule spiderRule = new SpiderRule();
            spiderRule.setIsGetText(false);

            //采用原先cookie采集
            cookies = MyStringUtil.json2cookie(analogLogin.getCookie());

            PhantomJSDriver driver = phantomJSDriverPool.borrowPhantomJSDriver();
            try {
                SpiderClaim spiderClaim = new SpiderClaim();
                spiderClaim.setPhantomJSDriver(driver);
                spiderClaim.setPipeline(spiderTemporaryRecordPipeline);
                spiderClaim.setCookieSet(cookies);
                spiderClaim.setSleepTime(3000);
                spiderPage.startSpider(linkId, analogLogin.getTargetUrl(), spiderClaim, spiderRule);
            } finally {
                phantomJSDriverPool.returnObject(driver);
            }

            //判断目标页是否爬取成功
            boolean flag = true;
            TemporaryRecordEntity temporaryRecord = temporaryRecordService.queryBylinkId(linkId);
            if (!MyStringUtil.urlCutParam(linkInfoEntity.getUrl())
                    .equals(MyStringUtil.urlCutParam(temporaryRecord.getUrl()))) {
                linkInfoEntity.setFailTimes(linkInfoEntity.getFailTimes() + 1);
                linkInfoService.update(linkInfoEntity);
                flag = false;
                if (linkInfoEntity.getFailTimes() % 3 == 0) {
                    linkInfoEntity.setHasTarget(Constant.VALUE_ZERO);
                    linkInfoEntity.setFailTimes(Constant.VALUE_ZERO);
                    linkInfoService.update(linkInfoEntity);
                }
            } else {
                if (linkInfoEntity.getFailTimes() != 0) {
                    linkInfoEntity.setFailTimes(Constant.VALUE_ZERO);
                    linkInfoService.update(linkInfoEntity);
                }
            }
            if (!flag) {
                //模拟登录，获取cookie
                PhantomJSDriver todriver = phantomJSDriverPool.borrowPhantomJSDriver();
                try {
                    cookies = loginAnalog.login(loginInfo.getId(), todriver);
                    //获取目标页
                    SpiderClaim spiderClaim = new SpiderClaim();
                    spiderClaim.setPhantomJSDriver(todriver);
                    spiderClaim.setPipeline(spiderTemporaryRecordPipeline);
                    spiderClaim.setCookieSet(cookies);
                    spiderClaim.setSleepTime(3000);
                    spiderPage.startSpider(linkId, loginInfo.getTargetUrl(), spiderClaim, spiderRule);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    phantomJSDriverPool.returnObject(todriver);
                }
            }

            //取得目标页
            LinkInfoEntity spiderLink = linkInfoService.queryById(linkId);
            SpiderRule rule = spiderRuleService.getOneById(spiderLink.getRuleId());

            //判断目标页是否采集成功

            //组装解析表头信息
            TemporaryRecordEntity te = new TemporaryRecordEntity();
            te.setLinkId(linkId);
            te.setUrl(spiderLink.getUrl());
            List<String> spiderHead = htmlprocess.process(te, rule);
            ResultInfoEntity resultInfo = new ResultInfoEntity();
            resultInfo.setSystem(spiderLink.getSystem());
            resultInfo.setModule(spiderLink.getModule());
            resultInfo.setCreateTime(new Date());
            resultInfo.setLinkId(spiderLink.getLinkId());
            resultInfoService.save(resultInfo);

            //采集到的表头插入数据库中
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String data = dateFormat.format(new Date());
            String informationName = "自动采集信息" + data;
            for (String vaule : spiderHead) {
                PageInfoEntity pageInfo = new PageInfoEntity();
                pageInfo.setNameCn(vaule);
                pageInfo.setResultId(resultInfo.getId());
                pageInfo.setInformationName(informationName);
                pageInfo.setNameEn(MyStringUtil.getPinYinHeadChar(vaule));
                pageInfoService.save(pageInfo);
            }
        }
    }
}
