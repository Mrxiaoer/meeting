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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 截图
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-02
 */
@Component
public class JieTu {

	@Value("${image.path}")
	private String imagePath;

	@Autowired
	private PhantomJSDriverPool phantomJSDriverPool;

	public Map<String, Object> savePage2Pic(String url, Set<Cookie> cookies, String fileName) {
		PhantomJSDriver phantomjsDriver = null;
		Map<String, Object> resultMap = new HashMap<>(4);
		try {
			phantomjsDriver = phantomJSDriverPool.borrowPhantomJSDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			assert (phantomjsDriver != null);
			String imageLocalPath = imagePath + fileName + ".jpg";
			try {
				phantomjsDriver.get(url);
				phantomjsDriver.manage().deleteAllCookies();

				for (Cookie cookie : cookies) {
					try {
						phantomjsDriver.manage().addCookie(cookie);
					} catch (UnableToSetCookieException utsce) {
						Cookie cook;
						if (cookie.getDomain().startsWith(".")) {
							cook = new Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain().substring(1), cookie.getPath(),
							                  cookie.getExpiry(), cookie.isSecure(), cookie.isHttpOnly());
						} else {
							cook = new Cookie(cookie.getName(), cookie.getValue(), "." + cookie.getDomain(), cookie.getPath(), cookie.getExpiry(),
							                  cookie.isSecure(), cookie.isHttpOnly());
						}
						phantomjsDriver.manage().addCookie(cook);
					}
				}
				phantomjsDriver.get(url);
				System.out.println(phantomjsDriver.manage().getCookies().toString());

				//放大图片
				phantomjsDriver.executeScript("document.body.getElementsByTagName(\"img\")[0].setAttribute('style', 'width: 100% !important')");
				File scrFile = phantomjsDriver.getScreenshotAs(OutputType.FILE);
				BufferedImage bufferedImage = ImageIO.read(scrFile);
				//压缩存储
				ImageUtil.scale(bufferedImage, new File(imageLocalPath), 0.5F);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			resultMap.put(SpiderConstant.IMAGE_PATH, imageLocalPath);
			resultMap.put(SpiderConstant.COOKIES, phantomjsDriver.manage().getCookies());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (phantomjsDriver != null) {
				phantomJSDriverPool.returnObject(phantomjsDriver);
			}
		}
		return resultMap;
	}

}