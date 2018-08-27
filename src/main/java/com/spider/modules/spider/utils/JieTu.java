package com.spider.modules.spider.utils;

import cn.hutool.core.util.ImageUtil;
import com.spider.modules.spider.config.PhantomJSDriverPool;
import com.spider.modules.spider.config.SpiderConstant;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.UnableToSetCookieException;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 截图
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-02
 */
@Component
public class JieTu {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${VcCode.image.path}")
	private String imagePath;

	@Autowired
	private PhantomJSDriverPool phantomJSDriverPool;

	public Map<String, Object> savePage2Pic(String url, Set<Cookie> cookies, String fileName) throws Exception {
		PhantomJSDriver phantomjsDriver = null;
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		Map<String, Object> resultMap = new HashMap<>(4);
		try {
			phantomjsDriver = phantomJSDriverPool.borrowPhantomJSDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			assert (phantomjsDriver != null);
			String imageLocalPath = imagePath + year + "-" + month + "-" + day + "/" + fileName + ".jpg";
			phantomjsDriver.get(url);
			phantomjsDriver.manage().deleteAllCookies();

			for (Cookie cookie : cookies) {
				try {
					phantomjsDriver.manage().addCookie(cookie);
				} catch (UnableToSetCookieException utsce) {
					Cookie cook;
					if (cookie.getDomain().startsWith(".")) {
						cook = new Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain().substring(1),
								cookie.getPath(), cookie.getExpiry(), cookie.isSecure(), cookie.isHttpOnly());
					} else {
						cook = new Cookie(cookie.getName(), cookie.getValue(), "." + cookie.getDomain(), cookie.getPath(),
								cookie.getExpiry(), cookie.isSecure(), cookie.isHttpOnly());
					}
					phantomjsDriver.manage().addCookie(cook);
				}
			}
			phantomjsDriver.get(url);

			//放大图片
			try {
				phantomjsDriver.executeScript(
						"document.body.getElementsByTagName(\"img\")[0].setAttribute('style', 'width: 100%;"
								+ "height: 100%')");
			} catch (Exception ex) {
				throw new Exception("无法获取验证码截图", ex);
			}
			File scrFile = phantomjsDriver.getScreenshotAs(OutputType.FILE);
			BufferedImage bufferedImage = ImageIO.read(scrFile);
			//压缩存储
			File destFile = new File(imageLocalPath);
			if (!destFile.exists()) {
				boolean mk = destFile.mkdirs();
				if (mk) {
					logger.info("创建验证码图片文件目录完成！");
				}
			}
			ImageUtil.scale(bufferedImage, destFile, 0.5F);

			resultMap.put(SpiderConstant.IMAGE_PATH, imageLocalPath);
			resultMap.put(SpiderConstant.COOKIES, phantomjsDriver.manage().getCookies());
		} finally {
			if (phantomjsDriver != null) {
				phantomJSDriverPool.returnObject(phantomjsDriver);
			}
		}
		return resultMap;
	}

	public String saveVc(PhantomJSDriver driver, String vcXpath, String fileName) throws Exception {
		assert (driver != null);
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		String imageLocalPath = imagePath + year + "-" + month + "-" + day + "/" + fileName + ".jpg";
		//放大图片
		int trynum = 0;
		boolean isOk = false;
		while (trynum < 5 && !isOk) {
			try {
				driver.executeScript("document.evaluate('" + vcXpath + "', document, null, XPathResult"
						+ ".FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.setAttribute('style', 'position: fixed;width: "
						+ "100%;" + "z-index: 9999;top: 0;height: 100%;left: 0;')");
				isOk = true;
			} catch (Exception ex) {
				Thread.sleep(500);
				trynum += 1;
			}
		}
		if (!isOk) {
			throw new Exception("无法获取验证码截图");
		}
		File scrFile = driver.getScreenshotAs(OutputType.FILE);
		BufferedImage bufferedImage = ImageIO.read(scrFile);
		try {
			driver.executeScript("document.evaluate('" + vcXpath + "', document, null, XPathResult"
					+ ".FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.removeAttribute('style', 'position: fixed;width: "
					+ "100%;z-index: 9999;top: 0;height: 100vh;left: 0;')");
		} catch (Exception ex) {
			throw new Exception("还原页面失败：", ex);
		}
		//压缩存储
		File destFile = new File(imageLocalPath);
		if (!destFile.exists()) {
			boolean mk = destFile.mkdirs();
			if (mk) {
				logger.info("创建验证码图片文件目录完成！");
			}
		}
		ImageUtil.write(ImageUtil.scale(bufferedImage, 450, 300), destFile);
		return imageLocalPath;
	}

}