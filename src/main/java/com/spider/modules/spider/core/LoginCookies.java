package com.spider.modules.spider.core;

import com.spider.modules.spider.config.PhantomJSDriverFactory;
import com.spider.modules.spider.entity.MySite;
import com.spider.modules.spider.utils.RunTimeout;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;

/**
 * 通过cookies登录
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-11-15
 */
@Service
public class LoginCookies {

    private Logger logger = LoggerFactory.getLogger(LoginAnalog.class);

    public PhantomJSDriver loginByCookies(PhantomJSDriver driver, MySite site, Request request) {
        assert (driver != null);

        WebDriver.Options manage = driver.manage();

        logger.info("开始设置cookie");
        long setCookieStartTime = System.currentTimeMillis();
        if (site.getCookies() != null) {
            for (Object o : site.getCookies().entrySet()) {
                Map.Entry<String, String> cookieEntry = (Map.Entry) o;
                Cookie cookie = new Cookie(cookieEntry.getKey(), cookieEntry.getValue());
                manage.addCookie(cookie);
            }
        }
        if (site.getAllCookies() != null) {
            Set<Map.Entry<String, Map<String, String>>> allCookieSet = site.getAllCookies().entrySet();

            boolean canSet;
            canSet = setCookies(manage, allCookieSet);
            if (!canSet) {
                driver.get(request.getUrl());
                manage.deleteAllCookies();
                canSet = setCookies(manage, allCookieSet);
            }
        }
        logger.info("设置cookie用时{}毫秒", System.currentTimeMillis() - setCookieStartTime);

        Class[] paramClzs = {String.class};
        Object[] paramObjs = {request.getUrl()};
        int timeOut = 7500;
        try {
            RunTimeout.timeoutMethod(driver, "get", paramClzs, paramObjs, timeOut);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return driver;
    }

    private boolean setCookies(WebDriver.Options manage, Set<Map.Entry<String, Map<String, String>>> allCookieSet) {
        boolean canSet = true;
        for (Map.Entry<String, Map<String, String>> cookiemap : allCookieSet) {
            for (Map.Entry<String, String> cookieEntry : cookiemap.getValue().entrySet()) {
                try {
                    manage.addCookie(
                            new Cookie(cookieEntry.getKey(), cookieEntry.getValue(), cookiemap.getKey(), "/", null));
                } catch (Exception e) {
                    try {
                        if (cookiemap.getKey().startsWith(".")) {
                            manage.addCookie(new Cookie(cookieEntry.getKey(), cookieEntry.getValue(),
                                    cookiemap.getKey().substring(1, cookiemap.getKey().length() - 1), "/", null));
                        } else {
                            manage.addCookie(
                                    new Cookie(cookieEntry.getKey(), cookieEntry.getValue(), "." + cookiemap.getKey(),
                                            "/", null));
                        }
                    } catch (Exception ex) {
                        canSet = false;
                        return canSet;
                    }
                }
            }
        }
        return canSet;
    }
}