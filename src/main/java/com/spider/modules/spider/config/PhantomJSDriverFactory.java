package com.spider.modules.spider.config;

import com.spider.modules.spider.utils.ReadProp;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

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
	public PhantomJSDriver create() throws Exception {
//		Map<String, String> prop = readProp.readp();
		Properties prop = readProp.readp();
//		Properties sConfig = new Properties();
//		String configFile = "selenium/config.ini";
//		if (System.getProperty("selenuim_config") != null) {
//			configFile = System.getProperty("selenuim_config");
//		}

//		sConfig.load(new FileReader(configFile));
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
		cliArgsCap.add("--load-images=false");
		// 本地缓存
		cliArgsCap.add("--disk-cache=true");
		dcaps.setCapability("phantomjs.cli.args", cliArgsCap);
		dcaps.setCapability("phantomjs.ghostdriver.cli.args",
		                    new String[]{"--logLevel=" + (prop.getProperty("phantomjs_driver_loglevel") != null ? prop.getProperty("phantomjs_driver_loglevel") : "INFO")});
		//驱动支持
		dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, prop.getProperty("phantomjs_exec_path"));
		PhantomJSDriver driver = new PhantomJSDriver(dcaps);
		driver.manage().window().maximize();
		return driver;
	}

	@Override
	public PooledObject<PhantomJSDriver> wrap(PhantomJSDriver phantomJSDriver) {
		return new DefaultPooledObject<PhantomJSDriver>(phantomJSDriver);
	}

}
