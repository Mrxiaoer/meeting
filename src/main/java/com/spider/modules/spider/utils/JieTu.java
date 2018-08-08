package com.spider.modules.spider.utils;

import cn.hutool.core.util.ImageUtil;
import com.spider.modules.spider.config.PhantomJSDriverPool;
import com.spider.modules.spider.config.SpiderConstant;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.UnableToSetCookieException;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 截图 ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-02
 */
@Component
public class JieTu {

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
						cook = new Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain().substring(1), cookie.getPath(), cookie.getExpiry(),
						                  cookie.isSecure(), cookie.isHttpOnly());
					} else {
						cook = new Cookie(cookie.getName(), cookie.getValue(), "." + cookie.getDomain(), cookie.getPath(), cookie.getExpiry(),
						                  cookie.isSecure(), cookie.isHttpOnly());
					}
					phantomjsDriver.manage().addCookie(cook);
				}
			}
			phantomjsDriver.get(url);

			//放大图片
			try {
				phantomjsDriver.executeScript(
						"document.body.getElementsByTagName(\"img\")[0].setAttribute('style', 'width: 100% !important;height: 100% !important')");
			} catch (Exception ex) {
				throw new Exception("无法获取验证码截图", ex);
			}
			File scrFile = phantomjsDriver.getScreenshotAs(OutputType.FILE);
			BufferedImage bufferedImage = ImageIO.read(scrFile);
			//压缩存储
			File destFile = new File(imageLocalPath);
			if (!destFile.exists()) {
				destFile.mkdirs();
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

}