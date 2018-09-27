package com.spider.modules.spider.config;

import com.spider.modules.spider.utils.ReadProp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.Configuration;

/**
 * phantomjs驱动工厂
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-19
 */
@Configuration
public class PhantomJSDriverFactory extends BasePooledObjectFactory<PhantomJSDriver> {

	@Resource
	private ReadProp readProp;

	@Override
	public PhantomJSDriver create() {
		Properties prop = readProp.readp();
		DesiredCapabilities dcaps = new DesiredCapabilities();
		//ssl证书支持
		dcaps.setCapability("acceptSslCerts", true);
		//截屏支持
		dcaps.setCapability("takesScreenshot", true);
		//css搜索支持
		dcaps.setCapability("cssSelectorsEnabled", true);
		//js支持
		dcaps.setJavascriptEnabled(true);
		ArrayList<String> cliArgsCap = new ArrayList<>(6);
		cliArgsCap.add("--web-security=false");
		cliArgsCap.add("--ssl-protocol=any");
		cliArgsCap.add("--ignore-ssl-errors=true");
		cliArgsCap.add("--load-images=true");
		// 本地缓存
		cliArgsCap.add("--disk-cache=false");
		dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
		dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[]{
				"--logLevel=" + (prop.getProperty("phantomjs_driver_loglevel") != null ? prop
						.getProperty("phantomjs_driver_loglevel") : "INFO")});
		//驱动支持
		dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
				prop.getProperty("phantomjs_exec_path"));
		PhantomJSDriver driver = new PhantomJSDriver(dcaps);
		driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(120, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.manage().window().setSize(new Dimension(1169, 582));
		return driver;
	}

	@Override
	public PooledObject<PhantomJSDriver> wrap(PhantomJSDriver phantomJSDriver) {
		return new DefaultPooledObject<>(phantomJSDriver);
	}

}
