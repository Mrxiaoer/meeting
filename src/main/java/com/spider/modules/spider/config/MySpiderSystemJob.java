package com.spider.modules.spider.config;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-20
 */
@Component
public class MySpiderSystemJob {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhantomJSDriverPool phantomJSDriverPool;

	/**
	 * 定时清除driver
	 */
	@Scheduled(cron = "0 0 0/3 * * *")
	public void cleanEveryDay() {
		//todo
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

	//测试用
//	@Scheduled(cron = "0/2 * * * * *")
	public void testclean() {
		//		try {
		//			long start = System.currentTimeMillis();
		//			PhantomJSDriver xxx = phantomJSDriverPool.borrowObject(500);
		//			System.out.println("获取driver实际耗时：" + (System.currentTimeMillis() - start) + "毫秒");
		//
		//			for(int i=0;i<10;i++){
		//				xxx.get("http://115.233.227.46:18080/zqdata/login");
		//			}
		//
		//			System.out.println(xxx.getWindowHandles());
		//			System.out.println(xxx.getCurrentUrl());
		//			phantomJSDriverPool.returnObject(xxx);
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
		PhantomJSDriver xxx;
		for (int i = 0; i < 1; i++) {
			try {
				xxx = phantomJSDriverPool.borrowObject();
				xxx.get("http://115.233.227.46:18080/zqdata/login");
				phantomJSDriverPool.returnObject(xxx);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(phantomJSDriverPool.listAllObjects());
		//		try {
		//			xxx = phantomJSDriverPool.borrowObject();
		//			xxx.quit();
		//			phantomJSDriverPool.invalidateObject(xxx);
		//
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
	}

}
