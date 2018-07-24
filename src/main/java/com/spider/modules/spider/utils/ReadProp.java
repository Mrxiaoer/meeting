package com.spider.modules.spider.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取配置
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-02
 */
@Component
public class ReadProp {

	@Value("${webmagic.selenuim_config}")
	private String selenuim_config;

	public Map<String, String> readp() {
		//new 一个Properties对象
		Properties p = new Properties();
		try {
			//加载一个文件输入流
			p.load(new FileInputStream(Thread.currentThread().getContextClassLoader().getResource("").getPath() + selenuim_config));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Enumeration em = p.propertyNames();
		Map<String, String> map = new HashMap<>();
		//如果存在更多元素
		while (em.hasMoreElements()) {
			//返回Object对象转换为String
			String key = (String) em.nextElement();
			//通过键获得值
			String value = p.getProperty(key);
			map.put(key, value);
		}
		return map;
	}
}
