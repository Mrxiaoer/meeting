package com.spider.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-25
 */
public class TestPhantomJsDriver {

    public static PhantomJSDriver getPhantomJSDriver() {
        //设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", false);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        //js支持
        dcaps.setJavascriptEnabled(true);
        //驱动支持
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "/Users/maoxinmin/phantomjs-2.1.1-macosx/bin/phantomjs");
        System.out.println(dcaps.getCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY));
        PhantomJSDriver driver = new PhantomJSDriver(dcaps);
        return driver;
    }

    public static void main(String[] args) {
        WebDriver driver = getPhantomJSDriver();
        driver.get("http://www.baidu.com");
        System.out.println(driver.getCurrentUrl());
    }

}
