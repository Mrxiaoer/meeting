package com.spider.modules.spider.config;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-20
 */
@Component
public class MySpiderSystemJob {

	private final PhantomJSDriverPool phantomJSDriverPool;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public MySpiderSystemJob(PhantomJSDriverPool phantomJSDriverPool) {
		this.phantomJSDriverPool = phantomJSDriverPool;
	}

	/**
	 * 定时清除driver
	 */
	@Scheduled(cron = "0 0 0/12 * * *")
	public void cleanEveryDay() {
		PhantomJSDriver theDriver;
		try {
			logger.info(phantomJSDriverPool.listAllObjects().toString());
			for (int i = 0; i < phantomJSDriverPool.getMaxTotal(); i++) {
				theDriver = phantomJSDriverPool.borrowObject();
				theDriver.quit();
				phantomJSDriverPool.invalidateObject(theDriver);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("定时清除driver失败！Exception：{}", e);
		}
		logger.info("定时清除driver完成！当前池中对象：{}", phantomJSDriverPool.listAllObjects());
	}

}
