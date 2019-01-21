package com.meeting.modules.job.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务测试(演示Demo)
 *
 * testTask为spring bean的名称
 *
 * @author maoxinmin
 */
@Component("testTask")
public class TestTask {
	private Logger logger = LoggerFactory.getLogger(getClass());

	public void test1(String params) {
		logger.info("我是带参数的test1方法，正在被执行，参数为：" + params);

	}

	public void test2() {
		logger.info("我是不带参数的test2方法，正在被执行");
	}
}
