package com.spider.tests;

import cn.hutool.core.util.ImageUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-08-21
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class VcTest {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void saveVc() throws Exception {
		String vcXpath = "/html/body/div/div/div/div[2]/form/div[3]/div/div/div[2]/img";
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
		cliArgsCap.add("--web-security=true");
		cliArgsCap.add("--ssl-protocol=any");
		cliArgsCap.add("--ignore-ssl-errors=true");
		cliArgsCap.add("--load-images=true");
		// 本地缓存
		cliArgsCap.add("--disk-cache=true");
		dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
		dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[]{"--logLevel=" + "DEBUG"});
		//驱动支持
		dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:/spider/phantomjs.exe");
		PhantomJSDriver driver = new PhantomJSDriver(dcaps);
		driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(120, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.manage().window().setSize(new Dimension(1920, 1080));
		driver.get("http://fast.demo.renren.io/#/login");
		//		Thread.sleep(2000);
		System.out.println(driver.getCurrentUrl());
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);

		String imageLocalPath = "C:/spider/vc-image/before/" + year + "-" + month + "-" + day + "/" + "test" + ".jpg";
		String basePagePath = "C:/spider/vc-image/before/" + year + "-" + month + "-" + day + "/" + "base" + ".jpg";
		File scrFile = driver.getScreenshotAs(OutputType.FILE);
		BufferedImage bufferedImage = ImageIO.read(scrFile);
		File destFile = new File(basePagePath);
//		if (!destFile.exists()) {
//			boolean mk = destFile.mkdirs();
//			if (mk) {
//				logger.info("创建验证码图片文件目录完成！");
//			}
//		}
//		ImageUtil.scale(bufferedImage, destFile, 0.5F);
		//放大图片
		int trynum = 0;
		boolean isOk = false;
		while (trynum < 5 && !isOk) {

			try {
				driver.executeScript("document.evaluate('" + vcXpath + "', document, null, XPathResult"
						+ ".FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.setAttribute('style', 'position: fixed;width: "
						+ "100%;"
						+ "z-index: 9999;top: 0;height: 100%;left: 0;')");
				isOk = true;
			} catch (Exception ex) {
				Thread.sleep(500);
				trynum += 1;
			}
		}
		System.out.println("===>尝试次数："+trynum);
		scrFile = driver.getScreenshotAs(OutputType.FILE);
		bufferedImage = ImageIO.read(scrFile);
		try {
			driver.executeScript("document.evaluate('" + vcXpath + "', document, null, XPathResult"
					+ ".FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.removeAttribute('style', 'position: fixed;width: "
					+ "100%;z-index: 9999;top: 0;height: 100vh;left: 0;')");
		} catch (Exception ex) {
			throw new Exception("还原页面失败：", ex);
		}
		//压缩存储
		destFile = new File(imageLocalPath);
		if (!destFile.exists()) {
			boolean mk = destFile.mkdirs();
			if (mk) {
				logger.info("创建验证码图片文件目录完成！");
			}
		}
		ImageUtil.scale(bufferedImage, destFile, 0.5F);
	}

}
